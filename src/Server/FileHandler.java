package Server;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

class FileHandler {	
	static Collection<String> getFilenamesInDirectory(String dir) {
    	if(new File(dir).listFiles() == null)
    		return new ArrayList<String>();
    	Collection<File> files =new ArrayList<>(Arrays.asList(
    			new File(dir).listFiles(
    			new FileFilter() {
    				public boolean accept(File f) {
    					return f.isFile();
    				}
    			})));
    	Collection<String> filenames = new ArrayList<>();
    	for(File f : files) {
    		filenames.add(f.getName());
    	}
    	return filenames;
    }
    
    static Collection<String> getFilenamesInDirectory(String dir, String substring){
    	if(new File(dir).listFiles() == null)
    		return new ArrayList<String>();
    	Collection<File> files =new ArrayList<>(Arrays.asList(
    			new File(dir).listFiles(
    			new FilenameFilter() {
    				public boolean accept(File path, String name) {
    					return name.contains(substring);
    				}
    			})));
    	Collection<File> nonFiles = new ArrayList<>();
    	for(File f : files) {
    		if(!f.isFile()) {
    			nonFiles.add(f);
    		}
    	}
    	files.removeAll(nonFiles);
    	Collection<String> filenames = new ArrayList<>();
    	for(File f : files) {
    		filenames.add(f.getName());
    	}
    	return filenames;
    }
}
