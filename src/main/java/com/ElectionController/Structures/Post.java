package com.ElectionController.Structures;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String postId;
    private String postDescription;
    private List<Contestant> contestants = new ArrayList<Contestant>();

    public String getPostId() {
        return this.postId;
    }

    public List<Contestant> getContestants() {
        return this.contestants;
    }

    public void setContestants(List<Contestant> contestants) {
        this.contestants = contestants;
    }
}
