package com.shuttlemap.web.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service("velocity")
public class VelocityScripting extends AbstractScripting {

	public static final String LANGUAGE = "velocity";

	private static final Log log = LogFactory.getLog(VelocityScripting.class);
	
	private VelocityEngine engine;
	private RuntimeInstance ri;
	private long templateModificationCheckInterval =60;
	
	
	public void init() throws Exception {
		Properties props = new Properties();
		props.put("velocimacro.permissions.allow.inline.local.scope", "true");
		props.put("velocimacro.arguments.strict", "true");
		props.put("directive.set.null.allowed", "true");
		props.put("input.encoding", "UTF-8");
		props.put("output.encoding", "UTF-8");
		engine = new VelocityEngine(props);
		ri = new RuntimeInstance();
	}
	
	
	
	public Object compile(final Object script) {
	    Template template = new Template();
	    long modified = 0;
	    String name = null;
	    try {
	      if (script instanceof URL) {
	        modified = ((URL) script).openConnection().getLastModified();
	        name = ((URL) script).getPath();
	      }
	      final long lastModified = modified;
	      ResourceLoader rl = new ResourceLoader(){
	        
	        public long getLastModified(
	            org.apache.velocity.runtime.resource.Resource resource) {
	          if (script instanceof URL) {
	            try {
	              return ((URL) script).openConnection().getLastModified();
	            } catch (IOException e) {
	            	log.warn(e.getMessage(), e);
	            }
	          }
	          return System.currentTimeMillis();
	        }
	        public InputStream getResourceStream(String source)
	        throws ResourceNotFoundException {
	          if (script instanceof URL) {
	            try {
	              return ((URL) script).openConnection().getInputStream();
	            } catch (IOException e) {
	            	log.warn(e.getMessage(), e);
	            }
	          }
	          throw new ResourceNotFoundException(source + " not found.");
	        }
	        public void init(ExtendedProperties configuration) {
	        }
	        public boolean isSourceModified(
	            org.apache.velocity.runtime.resource.Resource resource) {
	          return resource.getLastModified() > lastModified;
	        }};
	        template.setResourceLoader(rl);
	        template.setEncoding("UTF-8");
	        template.setLastModified(lastModified);
	        template.setName(name);
	        template.setRuntimeServices(ri);
	        template.process();
	        template.setModificationCheckInterval(templateModificationCheckInterval);
	        return template;
	    } catch (Exception ex) {
	    	log.error(ex.getMessage(), ex);
	      return null;
	    }
	}


	  public <T> T evaluate(Object script,
	      Map<String, ? extends Object> variables, Class<T>... resultTypes) {

	    if (script==null) throw new IllegalArgumentException("script Text can't be null"); 
	    VelocityContext velocityContext = new VelocityContext(variables);
	    Object writerObj = variables.get("writer");
	    Writer writer = null;
	    if (writerObj != null && writerObj instanceof Writer) {
	      writer = (Writer) writerObj;
	    }
	    if (writer == null) {
	      writer = new StringWriter();
	    }
	    try {
	      if (script instanceof Template) {
	        Template template = (Template) script;
	        if (template.requiresChecking()) {
	          template.setLastModified(template.getResourceLoader().getLastModified(template));
	          if (template.isSourceModified()) {
	            template.process();
	          }
	        }
	        template.merge(velocityContext, writer);
	      } else {
	        Reader reader = null;
	        try {
	          InputStream inputStream = null;
	          if (script instanceof String) {
	            reader = new StringReader((String)script);
	          } else if (script instanceof Reader) {
	            reader = (Reader) script;
	          } else if (script instanceof URL) {
	            inputStream = ((URL) script).openStream();
	          } else if (script instanceof Resource) {
	            inputStream = ((Resource) script).getInputStream();
	          }
	          if (inputStream != null) {
	            reader = new InputStreamReader(inputStream, "UTF-8");
	          }
	        } catch (Exception e) {
	          throw new IllegalArgumentException("Unable to load " + script +
	              " because " + e, e);
	        }
	        if (reader == null) {
	          throw new IllegalArgumentException("Only String and Reader object is allowed " +
	              "as the script. Received type was : " + script.getClass());
	        }
	        engine.evaluate(velocityContext, writer, this.getClass().getName(), reader);
	      }    
	    } catch (Exception ex) {
	      String msg = "Unable to execute " + script + " because " + ex;
	      log.error(msg, ex);
	      throw new IllegalArgumentException(msg, ex); 
	    }
	    return (T) writer.toString();
	  }

	public void exitContext() {
	}

	public void startContext(Object globalScript) {
		throw new UnsupportedOperationException();
	}

	public String getLanguage() {
		return LANGUAGE;
	}

}
