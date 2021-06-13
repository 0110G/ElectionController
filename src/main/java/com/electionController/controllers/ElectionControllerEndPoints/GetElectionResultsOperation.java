package com.electionController.controllers.ElectionControllerEndPoints;

import com.electionController.constants.ControllerOperations;
import com.electionController.constants.ResponseCodes;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.logger.ConsoleLogger;
import com.electionController.structures.APIParams.GetElectionResultsQuery;
import com.electionController.structures.Contestant;
import com.electionController.structures.ElectionResults;
import com.electionController.structures.Post;
import com.electionController.structures.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
public class GetElectionResultsOperation extends ElectionController{


    // Returns the result of the election
    /**
     * @Params: GetElectionResultsQuery
     *  @Required: voterId
     *  @Required: voterPassword
     *  @Required: election
     */
    private static final ControllerOperations ACTION = ControllerOperations.GET_ELECTION_RESULTS;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("/GetResults")
    public Response GetResults(@RequestBody GetElectionResultsQuery getElectionResultsQuery) {

        ValidateNotNull(getElectionResultsQuery);

        try {
            authenticationFacade.validateVoterCredentials(getElectionResultsQuery.getVoterId(),
                    getElectionResultsQuery.getVoterPassword());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), getElectionResultsQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        try {
            authenticationFacade.validateElectionViewer(getElectionResultsQuery.getVoterId(),
                    getElectionResultsQuery.getElectionId());
        } catch (RestrictedActionException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), getElectionResultsQuery);
            throw new RestrictedActionException("Not eligible to view election results");
        }

        List<Post> postList = dbGetter.getElectionPosts(getElectionResultsQuery.getElectionId());
        ElectionResults electionResults = mapPostsToElectionResults(postList,
                getElectionResultsQuery.getElectionId());
        return new Response.Builder()
                .withResponse(electionResults)
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .build();

    }

    private ElectionResults mapPostsToElectionResults(final List<Post> postList, final String electionId) {
        ElectionResults electionResults = new ElectionResults();
        electionResults.setElectionId(electionId);
        electionResults.setElectionTitle("Results");
        electionResults.setElectionDescription("Results");

        for (Post post : postList) {
            ElectionResults.PostResult p = ElectionResults.PostResult.builder()
                    .withPostId(post.getPostId())
                    .withContestantList(post.getContestants())
                    .withPostWinCriteria(post.getWinCriteria())
                    .withPostDescription(post.getPostDescription())
                    .build();

            Collections.sort(p.getContestantList(), new CustomComparator());

            int current = 1;
            p.getContestantList().get(0).setRank(1);
            int prevVotes = p.getContestantList().get(0).getVotesSecured();
            for (int i=1 ; i<p.getContestantList().size() ; i++) {
                if (p.getContestantList().get(i).getVotesSecured() == prevVotes) {
                    p.getContestantList().get(i).setRank(current);
                } else {
                    current++;
                    p.getContestantList().get(i).setRank(current);
                }
                prevVotes = p.getContestantList().get(i).getVotesSecured();
            }
            electionResults.getElectionPostResults().add(p);
        }
        return electionResults;
    }

    public class CustomComparator implements Comparator<Contestant> {
        private Post.WinCriteria winCriteria;
        private
        CustomComparator() {this.winCriteria = Post.WinCriteria.GREATEST_NUMBER_OF_VOTES;}
        CustomComparator(Post.WinCriteria winCriteria) {this.winCriteria = winCriteria;}

        @Override
        public int compare(Contestant c1, Contestant c2) {
            switch (winCriteria) {
                case MAJORITY:
                    return 0;
                case LOWEST_NUMBER_OF_VOTES:
                    return c1.getVotesSecured() - c2.getVotesSecured();
                case GREATEST_NUMBER_OF_VOTES:
                    return c2.getVotesSecured() - c1.getVotesSecured();
                default:
                    return c1.getVotesSecured() - c2.getVotesSecured();
            }
        }
    }

}
