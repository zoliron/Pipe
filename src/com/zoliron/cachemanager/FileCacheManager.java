package com.zoliron.cachemanager;

import com.zoliron.utils.IoUtils;

import java.io.*;
import java.nio.file.Paths;
import java.util.Base64;



/**
 * A {@link File} base {@link CacheManager}.
 *
 * @author Ronen Zolicha
 */
public class FileCacheManager implements CacheManager{



	/**
	 * The cache directory.
	 */
	private static final File CACHE_DIRECTORY = Paths.get("file_cache").toFile();



	@Override
	public void save(String problem, String solution) throws IOException{
		if (!CACHE_DIRECTORY.exists() && !CACHE_DIRECTORY.mkdirs())
			throw new IOException("Failed to create the file cache directory at: " + CACHE_DIRECTORY.getAbsolutePath());

		File file = getProblemFile(problem);
		try (InputStream is = IoUtils.toInputStream(solution);
			 OutputStream os = new BufferedOutputStream(new FileOutputStream(file))){

			IoUtils.pump(is, os);
		}
	}



	@Override
	public String load(String problem) throws IOException{
		File file = getProblemFile(problem);
		if (!file.exists())
			return null;

		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))){
			return IoUtils.readAsString(bis);
		}
	}



	/**
	 * Returns the problem file.
	 */
	private static File getProblemFile(String problem){
		String name = Base64.getEncoder().withoutPadding().encodeToString(problem.getBytes());
//		String name = UUID.nameUUIDFromBytes(problem.getBytes()).toString();
		String fileName = name + ".data";

		return new File(CACHE_DIRECTORY, fileName);
	}



}