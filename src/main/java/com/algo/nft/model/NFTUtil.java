package com.algo.nft.model;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.algo.files.FileSystemStorageService;

public class NFTUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.algo.nft.model.NFTUtil");
	private FileSystemStorageService storageService;
	public NFTUtil(FileSystemStorageService storageService) {
		super();
		this.storageService = storageService;
	}
	public String saveImageAndGetURL(String prefix, String key, String imageAsString) {
		try {
			if(imageAsString == null || imageAsString.trim().isEmpty())
				return "NA";
				long crc32Checksum = getCRC32Checksum(key);
				String serverImageURL = storageService.storeImageAsString(imageAsString, prefix+"_"+crc32Checksum);
				if(serverImageURL == null || serverImageURL.trim().isEmpty())
					throw new Exception("Error saving image to server");
				return serverImageURL;
		}catch(Exception exception) {
			LOGGER.error("Error while saving the image NFTUtil.saveImageAndGetURL key", key, exception);
			return null;
		}
	}
	public static long getCRC32Checksum(String inputValue) {			 
		byte[] bytes = inputValue.getBytes();
	    Checksum crc32 = new CRC32();
	    crc32.update(bytes, 0, bytes.length);
	    return crc32.getValue();
	}
}
