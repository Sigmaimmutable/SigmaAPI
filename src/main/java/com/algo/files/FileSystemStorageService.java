package com.algo.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class FileSystemStorageService{
	
	private final Path rootLocation;

	public FileSystemStorageService(String pathLocation) {
		this.rootLocation = Paths.get(pathLocation);
	}
	private String getTargetFolder() {
	Calendar instance = Calendar.getInstance();
	DateFormat targetFormat = new SimpleDateFormat("dd_MM_yyyy");
	String format = targetFormat.format(instance.getTime());
	return format;
	}
	public Optional<String> getExtensionByStringHandling(String filename) {
	    return Optional.ofNullable(filename)
	      .filter(f -> f.contains("."))
	      .map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}
	public String store(MultipartFile file, long crc32Checksum) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}
			Path resolve = this.rootLocation.resolve(getTargetFolder());
			File file2 = resolve.toFile();
			if(!file2.exists())
				file2.mkdir();
			Path destinationFile = resolve.resolve(Paths.get(crc32Checksum+"."+FilenameUtils.getExtension(file.getOriginalFilename()))).normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(resolve.toAbsolutePath())) {
				// This is a security check
				throw new StorageException("Cannot store file outside current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
				String targetFile = destinationFile.toFile().getAbsolutePath();
				return targetFile;
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}
	
	   public String readFileToString(String path) {
	        ResourceLoader resourceLoader = new DefaultResourceLoader();
	        Resource resource = resourceLoader.getResource(path);
	        return asString(resource);
	    }

	    public String asString(Resource resource) {
	        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
	            return FileCopyUtils.copyToString(reader);
	        } catch (IOException e) {
	            throw new UncheckedIOException(e);
	        }
	    }
	    @Deprecated
	    public String storeImageAsString(String imageAsString, long crc32Checksum) {
	    	return storeImageAsString(imageAsString, crc32Checksum+"");
	    }
	
	public String storeImageAsString(String imageAsString, String crc32Checksum) {
		try {
			if (imageAsString.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}
			Path resolve = this.rootLocation.resolve(getTargetFolder());
			File file2 = resolve.toFile();
			if(!file2.exists())
				file2.mkdir();
			Path destinationFile = resolve.resolve(Paths.get(crc32Checksum+".txt")).normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(resolve.toAbsolutePath())) {
				// This is a security check
				throw new StorageException("Cannot store file outside current directory.");
			}
				Files.write( destinationFile, imageAsString.getBytes());
				String targetFile = destinationFile.toFile().getAbsolutePath();
				return targetFile;
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation)).map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}
	}
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException("Could not read file: " + filename);
			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
