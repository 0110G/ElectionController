package com.electionController.controllers.electionController;

import com.electionController.constants.ControllerOperation;
import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ActionController;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.structures.APIParams.GetElectionResultsQuery;
import com.electionController.structures.Contestant;
import com.electionController.structures.ElectionResults;
import com.electionController.structures.Post;
import com.electionController.structures.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.electionController.controllers.electionController.ElectionController.ValidateNotNull;

@RestController
public class GetElectionResultsOperation extends ActionController<GetElectionResultsQuery, Response> {

    private static final ControllerOperation ACTION = ControllerOperation.GET_ELECTION_RESULTS;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public ControllerOperation getControllerOperation() {
        return this.ACTION;
    }

    @Override
    @PostMapping("/GetResults")
    public Response execute(@RequestBody GetElectionResultsQuery getElectionResultsQuery) {
        return super.execute(getElectionResultsQuery);
    }

    @Override
    protected Response executeAction(final GetElectionResultsQuery getElectionResultsQuery) {
        return this.getResults(getElectionResultsQuery);
    }

    @Override
    public void validateActionAccess(final GetElectionResultsQuery getElectionResultsQuery) {
        ValidateNotNull(getElectionResultsQuery);
        authenticationFacade.validateVoterCredentials(getElectionResultsQuery.getVoterId(),
                getElectionResultsQuery.getVoterPassword());
        authenticationFacade.validateElectionViewer(getElectionResultsQuery.getVoterId(),
                getElectionResultsQuery.getElectionId());
    }

    private Response getResults(final GetElectionResultsQuery getElectionResultsQuery) {
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
