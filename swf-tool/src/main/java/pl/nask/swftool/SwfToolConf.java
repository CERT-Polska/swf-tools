/*
 * Copyright (c) NASK, NCSC
 * 
 * This file is part of HoneySpider Network 2.1.
 * 
 * This is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.nask.swftool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import pl.nask.swftool.share.ToolException;

public class SwfToolConf {
	// default conf.file
	private static final String ADDRESS_D = "localhost";
	private static final String PORT_D = "54321";
	private static final String SHARED_PATH_D = "./";

	// default conf.prog
	private static final String SHARED_LOG_D = "log/";
	private static final String SHARED_PLUGINS_D = "plugins/";
	private static final String SHARED_INPUT_D = "input/";
	private static final String SHARED_CONF_D = "conf/";

	// default files
	private static final String CONF_FILE_D = "SWF-TOOL.conf";
	private static final String LOG_RES_FILE_D = "swf-tool.res";
	private static final String LOG_LOG_FILE_D = "swf-tool.log";

	private static final String SHARED_FOLDER_STRING = "shared_folder";
	static final String LOG_PROPERTIES_FILE = "log4j.properties";

	private static Properties toolProperties = new Properties();

	public SwfToolConf(String path) throws ToolException {
		if (checkFileExist(path)) {
			loadProperties(path);
		} else {
			throw new ToolException(true, "Check Path to Config ");
		}

		checkFoldersTree();
	}

	public SwfToolConf() throws ToolException {
		super();

		// conf
		try {
			String configDir = SHARED_PATH_D + SHARED_CONF_D;

			if (checkFileExist(configDir, CONF_FILE_D)) {
				loadProperties(configDir + CONF_FILE_D);
			} else if (checkFolderExist(configDir)) {
				createFile(configDir, CONF_FILE_D);
				setProperties(configDir + CONF_FILE_D);
				loadProperties(configDir + CONF_FILE_D);
			} else {
				createFolders(configDir);
				createFile(configDir, CONF_FILE_D);
				setProperties(configDir + CONF_FILE_D);
				loadProperties(configDir + CONF_FILE_D);
			}

			checkFoldersTree();

		} catch (IOException ex) {
			throw new ToolException(true, "Error1", ex);
		}
	}

	public final String getAddressString() {
		return toolProperties.getProperty("address");
	}

	public final String getPortString() {
		return toolProperties.getProperty("port");
	}

	public final String getPluginsPathString() {
		return toolProperties.getProperty(SHARED_FOLDER_STRING) + SHARED_PLUGINS_D;
	}

	public final String getInputPathString() {
		return toolProperties.getProperty(SHARED_FOLDER_STRING) + SHARED_INPUT_D;
	}

	public final String getLogPathString() {
		return toolProperties.getProperty(SHARED_FOLDER_STRING) + SHARED_LOG_D;
	}

	public final String getLogFileLogString() {
		return SHARED_PATH_D + SHARED_LOG_D + LOG_LOG_FILE_D;
	}

	public final String getLogFileResString() {
		return toolProperties.getProperty(SHARED_FOLDER_STRING) + SHARED_LOG_D + LOG_RES_FILE_D;
	}

	public final List<String> getPluginList() {
		List<String> pluginList = new ArrayList<String>();
		File fileO = new File(getPluginsPathString());
		File[] fileTab = fileO.listFiles();
		for (File file : fileTab) {
			pluginList.add(file.getPath());
		}
		return pluginList;
	}

	private void loadProperties(String proPath) {
		File fileO = new File(proPath);
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(fileO);
			toolProperties.load(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(inputStream);
		}
	}

	private void close(InputStream is) {
		try {
			if (is != null)
				is.close();
		} catch (IOException e) {
			// failed to close IS.
		}
	}

	private void setProperties(String proPath) {
		try {
			FileWriter outFile = new FileWriter(proPath);
			PrintWriter out = new PrintWriter(outFile);

			out.println("address=" + ADDRESS_D);
			out.println("port=" + PORT_D);
			out.println("shared_folder=" + SHARED_PATH_D);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Boolean checkFileExist(String directory, String filename) {
		File fileO = new File(directory, filename);
		return fileO.exists();
	}

	private Boolean checkFileExist(String path) {
		File fileO = new File(path);
		return fileO.exists();
	}

	private Boolean checkFolderExist(String pathFile) {
		return (new File(pathFile)).isDirectory();
	}

	private Boolean createFolders(String pathFile) {
		return (new File(pathFile)).mkdirs();
	}

	private Boolean createFile(String pathFile, String fileName) throws IOException {
		File fileO = new File(pathFile + fileName);
		if (fileO.exists()) {
			return false;
		} else {
			return fileO.createNewFile();
		}
	}

	private void checkFoldersTree() {
		if (!checkFolderExist(toolProperties.getProperty(SHARED_FOLDER_STRING) + SHARED_PLUGINS_D))
			createFolders(toolProperties.getProperty(SHARED_FOLDER_STRING) + SHARED_PLUGINS_D);
		if (!checkFolderExist(toolProperties.getProperty(SHARED_FOLDER_STRING) + SHARED_INPUT_D))
			createFolders(toolProperties.getProperty(SHARED_FOLDER_STRING) + SHARED_INPUT_D);
	}

	public final void printToLogger(Logger logger) {
		logger.info(getAddressString());
		logger.info(getPortString());
		// directory with swf files:
		logger.info(getInputPathString());

		// plugins dir:
		logger.info(getPluginsPathString());

		logger.info(getLogPathString());

		logger.info(getLogFileLogString());
		logger.info(getLogFileResString());

		logger.info(getPluginList());
	}
}
