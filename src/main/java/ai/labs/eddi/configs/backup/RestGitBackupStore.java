package ai.labs.eddi.configs.backup;


import ai.labs.eddi.configs.backup.model.GitBackupSettings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class RestGitBackupStore implements IGitBackupStore {

    private final String gitSettingsPath = System.getProperty("user.dir") + "/gitsettings/";

    @Override
    public GitBackupSettings readSettings() {
        GitBackupSettings internalSettings = readSettingsInternal();
        internalSettings.setUsername("****");
        internalSettings.setPassword("****");
        return internalSettings;
    }

    @Override
    public void storeSettings(GitBackupSettings settings) {


        Properties properties = new Properties();
        try {
            if (!Files.exists(Paths.get(gitSettingsPath))) Files.createDirectories(Paths.get(gitSettingsPath));

            properties.setProperty("git.branch", settings.getBranch());
            properties.setProperty("git.commiter_email", settings.getCommitterEmail());
            properties.setProperty("git.commiter_name", settings.getCommitterName());
            properties.setProperty("git.password", settings.getPassword());
            properties.setProperty("git.username", settings.getUsername());
            properties.setProperty("git.repository_url", settings.getRepositoryUrl());
            properties.setProperty("git.description", settings.getDescription());
            properties.setProperty("git.isautomatic", String.valueOf(settings.isAutomatic()));

            OutputStream os = new FileOutputStream(new File(gitSettingsPath + "settings.properties"));
            properties.store(os, "autogenerated by EDDI");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GitBackupSettings readSettingsInternal() {
        Properties properties = new Properties();
        try {
            InputStream is = new FileInputStream(new File(gitSettingsPath + "settings.properties"));
            properties.load(is);

            GitBackupSettings settings = new GitBackupSettings();
            settings.setBranch(properties.getProperty("git.branch"));
            settings.setCommitterEmail(properties.getProperty("git.commiter_email"));
            settings.setCommitterName(properties.getProperty("git.commiter_name"));
            settings.setPassword(properties.getProperty("git.password"));
            settings.setUsername(properties.getProperty("git.username"));
            settings.setRepositoryUrl(properties.getProperty("git.repository_url"));
            settings.setDescription(properties.getProperty("git.description"));
            settings.setAutomatic(Boolean.parseBoolean(properties.getProperty("git.isautomatic")));
            return settings;
        } catch (FileNotFoundException e) {
            GitBackupSettings settings = new GitBackupSettings();
            settings.setDescription("No Git settings found");
            return settings;
        } catch (IOException e) {
            GitBackupSettings settings = new GitBackupSettings();
            settings.setDescription("No Git settings found");
            return new GitBackupSettings();
        }
    }
}
