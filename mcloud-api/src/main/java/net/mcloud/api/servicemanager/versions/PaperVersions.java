package net.mcloud.api.servicemanager.versions;

public enum PaperVersions {
    PAPER_1_18_2("https://api.papermc.io/v2/projects/paper/versions/1.18.2/builds/387/downloads/paper-1.18.2-387.jar");

    private String downloadUrl;

    PaperVersions(String url) {
        this.downloadUrl = url;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
