package com.electionController.structures;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String postId;      // Autogenerate
    private String electionId;  // Felt neccessary, might delete later
    private int postIndex;
    private String postDescription;
    private List<Contestant> contestants = new ArrayList<Contestant>();
    private WinCriteria winCriteria = WinCriteria.GREATEST_NUMBER_OF_VOTES;
    private int totalContestants;

    public enum WinCriteria {
        GREATEST_NUMBER_OF_VOTES(0),
        LOWEST_NUMBER_OF_VOTES(1),
        MAJORITY(2);

        private final int code;

        private WinCriteria(final int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public static WinCriteria getWinCriteria(final int code) {
            switch (code) {
                case 0:
                    return GREATEST_NUMBER_OF_VOTES;
                case 1:
                    return LOWEST_NUMBER_OF_VOTES;
                case 2:
                    return MAJORITY;
                default:
                    return GREATEST_NUMBER_OF_VOTES;
            }
        }

    }

    public Post() {
        this.postId = "0";
        this.postDescription = "Default Post";
    }

    public String getPostId() {
        return this.postId;
    }

    public void setPostId(final String postId) {
        this.postId = postId;
    }

    public String getElectionId() {
        return this.electionId;
    }

    public void setElectionId(String electionId) {
        this.electionId = electionId;
    }

    public String getPostDescription() {
        return this.postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public List<Contestant> getContestants() {
        return this.contestants;
    }

    public void setContestants(final List<Contestant> contestants) {
        this.contestants = contestants;
    }

    public WinCriteria getWinCriteria() {
        return this.winCriteria;
    }

    public void setWinCriteria(WinCriteria winCriteria) {
        this.winCriteria = winCriteria;
    }

    public int getTotalContestants() {
        if (this.contestants == null) {return 0;}
        return this.contestants.size();
    }

    public void setTotalContestants(int totalContestants) {
        this.totalContestants = totalContestants;
    }

    public void setPostIndex(int postIndex) {
        this.postIndex = postIndex;
    }

    public int getPostIndex() {
        return this.postIndex;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId='" + postId + '\'' +
                ", electionId='" + electionId + '\'' +
                ", postDescription='" + postDescription + '\'' +
                ", contestants=" + contestants +
                ", winCriteria=" + winCriteria +
                '}';
    }
}
