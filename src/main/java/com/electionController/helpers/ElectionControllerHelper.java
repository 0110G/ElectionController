package com.electionController.helpers;

import com.electionController.dbConnector.Deleter.DBDeletor;
import com.electionController.dbConnector.Getter.DBGetter;
import com.electionController.dbConnector.Putter.DBPutter;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.structures.Post;
import com.electionController.structures.Voter;
import com.electionController.structures.VoterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ElectionControllerHelper {

    @Autowired
    private DBPutter dbPutter;

    @Autowired
    private DBGetter dbGetter;

    @Autowired
    private DBDeletor dbDeletor;

    // Always called after the following authentications:
    // 1: The admin credentials are authentic
    // 2: The election id is authentic
    // 3: The query requester is election admin
    public void addVotersToElection(final List<String> voters, final String electionId, final String electionAdmin) {
        voters.add(electionAdmin);
        for (String voterId : voters) {
            if (voterId.equals(electionAdmin)) {
                continue;
            }
            VoterMap voterMap = new VoterMap();
            voterMap.setVoterEligible(true);
            voterMap.setVoterAdmin(false);
            voterMap.setVoterId(voterId);
            voterMap.setElectionId(electionId);
            dbPutter.registerVoterForElection(voterMap);
        }
    }

     public void deleteVotersFromElection(final List<String> votersToDelete,
                                          final String electionId,
                                          final String electionAdmin,
                                          boolean strictDelete) {
        List<Post>registeredPost = dbGetter.getElectionPosts(electionId);
        for (String voterId : votersToDelete) {
            if (electionAdmin.equals(voterId)) {continue;}
            if (strictDelete) {
                dbDeletor.deleteVoterFromElection(voterId, electionId);
                List<String> reg = getPostsForWhichVoterIsCandidate(registeredPost, voterId);
                for (String postId : reg) {
                    dbDeletor.deleteCandidateFromPost(postId, voterId);
                }
            } else {
                List<String> reg = getPostsForWhichVoterIsCandidate(registeredPost, voterId);
                if (reg.isEmpty()) {
                    dbDeletor.deleteVoterFromElection(voterId, electionId);
                }
            }
        }
    }

    List<String> getPostsForWhichVoterIsCandidate(final List<Post> posts, final String voterId) {
        List<String> postIds = new ArrayList<>();
        for (Post post : posts) {
            if (post.getContestants()
                    .stream()
                    .map(Voter::getVoterId)
                    .collect(Collectors.toList())
                    .contains(voterId)){
                postIds.add(post.getPostId());
            }
        }
        return postIds;
    }
}
