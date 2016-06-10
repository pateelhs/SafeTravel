package com.agiledge.keocometemployee.Model;

public class Trips {
    private String CustomerName, thumbnailUrl;
   private String time;
    private String issue;
    private String priority;
    private String jobid;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Trips() {

    }

    public void setCustomerName(String customerName) {
        this.CustomerName = customerName;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public Trips(String name, String thumbnailUrl, String time, String issue,
                 String priority, String jobid) {
        this.CustomerName = name;
        this.thumbnailUrl = thumbnailUrl;
        this.time = time;

        this.issue = issue;
        this.priority = priority;
        this.jobid=jobid;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setTitle(String name) {
        this.CustomerName = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getPriority(){return priority;}

    public void setPriority(String priority) {this.priority=priority;}


}