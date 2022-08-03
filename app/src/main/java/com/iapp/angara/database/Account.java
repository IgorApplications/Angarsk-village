package com.iapp.angara.database;

import java.util.Objects;

public class Account implements Element {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String MUTED = "muted";
    public static final String BANNED = "banned";
    public static final String MODERATOR = "moderator";
    public static final String REPUTATION = "reputation";
    public static final String START_REPUTATION = "No violations";

    private long id;
    private String name;
    private String email;
    private boolean muted;
    private boolean banned;
    private boolean moderator;
    private String reputation;

    public Account() {}

    public Account(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        reputation = START_REPUTATION;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isBanned() {
        return banned;
    }

    public void ban() {
        banned = true;
    }

    public void unban() {
        banned = false;
    }

    public boolean isMuted() {
        return muted;
    }

    public void mute() {
        muted = true;
    }

    public void unmute() {
        muted = false;
    }

    public boolean isModerator() {
        return moderator;
    }

    public void makeModerator() {
        moderator = true;
    }

    public String getReputation() {
        return reputation;
    }

    public void setReputation(String reputation) {
        this.reputation = reputation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && moderator == account.moderator && Objects.equals(name, account.name) && Objects.equals(email, account.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, moderator);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", moderator=" + moderator +
                '}';
    }
}
