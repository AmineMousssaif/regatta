package domain;

import be.ucll.ip.minor.groep124.model.Team;


public class TeamBuilder {

    private String name;

    private String category;

    private String club;

    private int passengers;

    private TeamBuilder aTeam() {
        return new TeamBuilder();
    }

    //VALID TEAMS
    public static TeamBuilder aValidTeam() {
        return new TeamBuilder()
                .withTeamName("TeamName")
                .withTeamCategory("categor")
                .withClub("Club")
                .withTeamMembers(5);
    }

    public static  TeamBuilder team1() {
        return new TeamBuilder()
                .withTeamName("TeamName1")
                .withTeamCategory("categor")
                .withClub("Club1")
                .withTeamMembers(5);
    }

    public static  TeamBuilder team2() {
        return new TeamBuilder()
                .withTeamName("TeamName2")
                .withTeamCategory("categor")
                .withClub("Club2")
                .withTeamMembers(5);
    }

    //INVALID TEAMS
    public static TeamBuilder anInvalidTeamWithNoName() {
        return new TeamBuilder()
                .withTeamName(null)
                .withTeamCategory("categor")
                .withClub("Club")
                .withTeamMembers(5);
    }

    public static TeamBuilder anInvalidTeamWithNoCategory() {
        return new TeamBuilder()
                .withTeamName("categor")
                .withTeamCategory(null)
                .withClub("Club")
                .withTeamMembers(5);
    }

    public static TeamBuilder aValidTeamWithNoClub() {
        return new TeamBuilder()
                .withTeamName("TeamName")
                .withTeamCategory("categor")
                .withClub(null)
                .withTeamMembers(5);
    }

    public static TeamBuilder anInvalidTeamWithNoTeamMembers() {
        return new TeamBuilder()
                .withTeamName("TeamName")
                .withTeamCategory("categor")
                .withClub("Club")
                .withTeamMembers(0);
    }

    public static TeamBuilder anInvalidTeamWithSpaceName() {
        return new TeamBuilder()
                .withTeamName(" ")
                .withTeamCategory("categor")
                .withClub("Club")
                .withTeamMembers(5);
    }

    //PARAMETERBUILDERS
    public TeamBuilder withTeamName(String teamName) {
        this.name = teamName;
        return this;
    }

    public TeamBuilder withTeamCategory(String teamCategory) {
        this.category = teamCategory;
        return this;
    }

    public TeamBuilder withClub(String club) {
        this.club = club;
        return this;
    }

    public TeamBuilder withTeamMembers(int teamMembers) {
        this.passengers = teamMembers;
        return this;
    }

    public Team build() {
        Team team = new Team();
        team.setName(name);
        team.setCategory(category);
        team.setClub(club);
        team.setPassengers(passengers);
        return team;
    }

}
