package com.ElectionController.Structures.APIParams;

public class NewVoterQuery {
    private String voterName;
    private String voterPassword;

    public String getVoterPassword() {
        return this.voterPassword;
    }

    public String getVoterName() {
        return this.voterName;
    }

    public static class Builder {
        private String voterName;
        private String voterPassword;

        public Builder withVoterName(final String voterName) {
            this.voterName = voterName;
            return this;
        }

        public Builder withVoterPassword(final String voterPassword) {
            this.voterPassword = voterPassword;
            return this;
        }

        public NewVoterQuery build() {
            NewVoterQuery newVoterQuery = new NewVoterQuery();
            newVoterQuery.voterName = this.voterName;
            newVoterQuery.voterPassword = this.voterPassword;
            return newVoterQuery;
        }
    }

    @Override
    public String toString() {
        return "NewVoterQuery{" +
                "voterName='" + voterName + '\'' +
                ", voterPassword='" + voterPassword + '\'' +
                '}';
    }
}
