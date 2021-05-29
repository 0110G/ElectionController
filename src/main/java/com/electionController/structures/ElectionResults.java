package com.electionController.structures;

import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

public class ElectionResults {
    private String electionId;
    private String electionTitle;
    private String electionDescription;
    private List<PostResult> electionPostResults = new ArrayList<>();

    public void setElectionId(String electionId) {
        this.electionId = electionId;
    }

    public String getElectionId() {
        return this.electionId;
    }

    public void setElectionTitle(String electionTitle) {
        this.electionTitle = electionTitle;
    }

    public String getElectionTitle() {
        return this.electionTitle;
    }

    public void setElectionDescription(String electionDescription) {
        this.electionDescription = electionDescription;
    }

    public String getElectionDescription() {
        return this.electionDescription;
    }

    public void setElectionPostResults(List<PostResult> electionPostResults) {
        if (electionPostResults != null) {
            this.electionPostResults = electionPostResults;
        }
    }

    public List<PostResult> getElectionPostResults() {
        return this.electionPostResults;
    }

    public static class PostResult {
        private String postId;
        private String postDescription;
        private Post.WinCriteria postWinCriteria;
        private List<Contestant> contestantList;
        private List<Contestant> postWinners;

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getPostId() {
            return this.postId;
        }

        public void setPostDecription(String postDecription) {
            this.postDescription = postDecription;
        }

        public String getPostDecription() {
            return this.postDescription;
        }

        public void setContestantList(List<Contestant> contestantList) {
            this.contestantList = contestantList;
        }

        public List<Contestant> getContestantList() {
            return this.contestantList;
        }

        public void setPostWinCriteria(Post.WinCriteria postWinCriteria) {
            this.postWinCriteria = postWinCriteria;
        }

        public Post.WinCriteria getPostWinCriteria() {
            return this.postWinCriteria;
        }

        public static Builder builder() {return new Builder();}

        public static class Builder {
            private String postId;
            private String postDescription;
            private Post.WinCriteria postWinCriteria = Post.WinCriteria.GREATEST_NUMBER_OF_VOTES;
            private List<Contestant> contestantList;
            private List<Contestant> postWinners;

            public Builder withPostId(final String postId) {
                this.postId = postId;
                return this;
            }

            public Builder withPostDescription(final String postDescription) {
                this.postDescription = postDescription;
                return this;
            }

            public Builder withPostWinCriteria(final Post.WinCriteria winCriteria) {
                if (winCriteria != null) {
                    this.postWinCriteria = winCriteria;
                }
                return this;
            }

            public Builder withContestantList(final List<Contestant> contestantList) {
                this.contestantList = contestantList;
                return this;
            }

            public PostResult build() {
                PostResult postResult = new PostResult();
                postResult.setPostId(this.postId);
                postResult.setPostDecription(this.postDescription);
                postResult.setContestantList(this.contestantList);
                postResult.setPostWinCriteria(this.postWinCriteria);
                return postResult;
            }
        }
    }

}
