package com.ElectionController.Structures;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class Voter {
    private String voterId;
    private String voterName;
    private String voterPassword;
    private List<VoterMap> electionList = new ArrayList<VoterMap>();

    public String getVoterId() {
        return this.voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getVoterName() {
        return this.voterName;
    }

    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }

    public String getVoterPassword() {
        return this.voterPassword;
    }

    public void setVoterPassword(String voterPassword) {
        this.voterPassword = voterPassword;
    }

    public List<VoterMap> getElectionList() {
        return this.electionList;
    }

    public void setElectionList(List<VoterMap> electionList) {
        this.electionList = electionList;
    }
}
