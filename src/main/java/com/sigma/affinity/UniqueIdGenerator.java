package com.sigma.affinity;

import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class UniqueIdGenerator {
	public static long getCRC32Checksum(byte[] bytes) {
	    Checksum crc32 = new CRC32();
	    crc32.update(bytes, 0, bytes.length);
	    return crc32.getValue();
	}
	public static String uuid() {
		UUID uuid =  UUID.randomUUID();
		String uuidAsString = uuid.toString();
		return uuidAsString;
	}
	public static void main(String[] args) {
		String value = "one 1";
		long crc32Checksum = getCRC32Checksum(value.getBytes());
		System.out.println(crc32Checksum);
		String value1 = "one 1";
		long crc32Checksum1 = getCRC32Checksum(value1.getBytes());
		System.out.println(crc32Checksum1);
	}
}
