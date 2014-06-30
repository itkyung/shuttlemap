package com.shuttlemap.android.common.bitmapDownloader;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.shuttlemap.android.utils.file.FileUtil;
import com.shuttlemap.android.utils.http.HttpUtil;
import com.shuttlemap.android.utils.image.ImageUtil;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapCacheManager{
	protected static BitmapCacheManager manager;
	//private Map<String,String> discCache;
	private final BitmapMemoryCache memoryCache;
	
	private final Md5FileNameGenerator fileNameGenerator;
	private int cacheSize;
	
	Context context;
	
	final String cacheDir;
	
	final int initMemCapacity = 200;
	final int initDiscCacheCapacity = 100;
	final int maxDiscCacheCapacity = 300;
	
	protected BitmapCacheManager(Context context){
		this.context = context;
		cacheSize = 16 * 1024 * 1024;
		
		//discCache   = Collections.synchronizedMap(new BitmapDiscCache(initDiscCacheCapacity, maxDiscCacheCapacity));
		memoryCache = new BitmapMemoryCache(cacheSize); 
		
		fileNameGenerator = new Md5FileNameGenerator();
		
		cacheDir = FileUtil.getCachePath(context);

//		File dir = new File(cacheDir);
//		File[] bitmapFiles = dir.listFiles();
//		
//		for (File file : bitmapFiles)
//		{
//			file.delete();
//		}
	}

	public static BitmapCacheManager createInstance(Context context)
	{
		manager = new BitmapCacheManager(context);
		return manager;
	}
	
	public static BitmapCacheManager getInstance()
	{
		return manager;
	}
	
	public Bitmap getBitmap(String url, BitmapFactory.Options options)
	{
		if (url == null)
			return null;

		// �޸𸮷κ��� ��Ʈ���� ��´�.
		Bitmap bitmap = memoryCache.get(url);
		
		// �̹����� �������� �ʴ� ���
		if (bitmap == null)
		{
			String fileName = fileNameGenerator.generate(url);
			String filePath = cacheDir + "/" + fileName;
			File file = new File(filePath);
			
			if (file.exists())
				bitmap = readBitmapFile(filePath, options);

			// ��ũ ĳ�� �Ǵ� ���� �ý��ۿ��� ��Ʈ���� ���� ���,
			// �޸� ĳ�� �������� �ű��.
			if (bitmap != null)
				memoryCache.put(url, bitmap);
		}
		
		return bitmap;
	}
	
	public void setBitmap(String url, Bitmap bitmap)
	{
		this.setBitmap(url, bitmap, false);
	}
	
	public void setBitmap(String url, Bitmap bitmap, boolean saveBitmap)
	{
		if (bitmap != null)
		{
			String fileName = this.fileNameGenerator.generate(url);
			
			if (saveBitmap)
				ImageUtil.saveBitmap(bitmap, cacheDir + File.separator + fileName);

			memoryCache.remove(url);
			memoryCache.put(url, bitmap);
		}
	}
	
	public int getCacheCount()
	{
		return memoryCache.getCurrentCacheSize();
	}

	public Bitmap downloadBitmap(String imageURL, BitmapFactory.Options options)
	{
		Bitmap bitmap = null;
		
		String filePath = cacheDir + File.separator + this.fileNameGenerator.generate(imageURL);
		HttpUtil.downloadImage(imageURL, filePath);
		
		bitmap = ImageUtil.loadFromFile(filePath, options);
		
		return bitmap;
	}
	
	public String generateCacheFilePath(String imageURL)
	{
		return this.cacheDir + File.separator + this.fileNameGenerator.generate(imageURL);
	}
	
	protected Bitmap readBitmapFile(String filePath, BitmapFactory.Options options)
	{
		Bitmap bitmap = null;
		
		try
		{
			bitmap = ImageUtil.loadFromFile(filePath, options);
		}
		catch(OutOfMemoryError e)
		{
			System.gc();
			
			try
			{
				bitmap = ImageUtil.loadFromFile(filePath, options);
			}
			catch(OutOfMemoryError e2)
			{
				e.printStackTrace();
			}
		}
		
		return bitmap;
	}
	
	public void clearCache(){
		ArrayList<String> keys = new ArrayList<String>();
	 	keys.addAll(memoryCache.getCacheKeyList());
		for(String key : keys){
			memoryCache.remove(key);
		}
	}
	
//	public void saveDiscCacheInfo()
//	{
//		String dirName = FileUtil.getDataPath(context);
//		String fileName = "disc_cache.dat";
//		
//		FileOutputStream fos = null;
//		ObjectOutputStream oos = null;
//		
//		try
//		{
//			File file = new File(dirName + File.separator + fileName);
//			fos = new FileOutputStream(file);
//			oos = new ObjectOutputStream(fos);
//			
//			oos.writeObject(discCache);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		finally
//		{
//			try
//			{
//				oos.close();
//				fos.close();
//			}
//			catch(Exception e)
//			{
//			}
//		}
//	}
//	
//	public void loadDiscCacheInfo()
//	{
//		String dirName = FileUtil.getDataPath(context);
//		String fileName = "disc_cache.dat";
//		
//		FileInputStream fis = null;
//		ObjectInputStream ois = null;
//		
//		try
//		{
//			File file = new File(dirName + File.separator + fileName);
//			fis = new FileInputStream(file);
//			ois = new ObjectInputStream(fis);
//
//			@SuppressWarnings("unchecked")
//			Map<String,String> cache = (Map<String,String>)ois.readObject();
//			
//			if (cache != null)
//			{
//				discCache = cache; // Collections.synchronizedMap(cache);
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		finally
//		{
//			try
//			{
//				ois.close();
//				fis.close();
//			}
//			catch(Exception e)
//			{
//			}
//		}
//	}
	
	
	class BitmapDiscCache extends LinkedHashMap<String, String> implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		int MAX_ENTRIES = 300;
		
		public BitmapDiscCache(int initialCapacity, int maxCapacity)
		{
			super(initialCapacity);
			MAX_ENTRIES = maxCapacity;
		}
		
		@Override
		protected boolean removeEldestEntry(Entry<String, String> eldest)
		{
			boolean result =  (size() > MAX_ENTRIES);
			
			if (result)
				removeCacheFile(eldest.getValue());
			
			return result;
		}

		@Override
		public String remove(Object key)
		{
			String fileName = get(key);
			removeCacheFile(fileName);
			
			return super.remove(key);
		}
		
		public void removeCacheFile(String fileName)
		{
			try
			{
				String filePath = cacheDir + File.separator + fileName;
				FileUtil.removeFile(filePath);
			}
			catch(Exception e) {}
		}
		
		private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
		{
			MAX_ENTRIES = in.readInt();
			
			int count = in.readInt();

			for (int i = 0; i < count; i++)
			{
				String key = (String)in.readObject();
				String val = (String)in.readObject();
				
				this.put(key, val);
			}
		}
		
		private void writeObject(java.io.ObjectOutputStream out) throws IOException
		{
			out.writeInt(MAX_ENTRIES);
			
			Iterator<String> it = this.keySet().iterator();
			
			out.writeInt(this.size());
			
			while (it.hasNext())
			{
				String key = it.next();
				String val = this.get(key);
				
				out.writeObject(key);
				out.writeObject(val);
			}
		}
	}

}
