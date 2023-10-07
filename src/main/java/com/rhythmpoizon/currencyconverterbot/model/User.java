package com.rhythmpoizon.currencyconverterbot.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import java.sql.Timestamp;

@EnableAutoConfiguration

@Entity(name="usersDataTable")
public class User {
    @Id
    private Long chatId;
    private String firstName;
    private String lastName;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getRegistredAt() {
        return registredAt;
    }

    public void setRegistredAt(Timestamp registredAt) {
        this.registredAt = registredAt;
    }

    private String userName;

    private Timestamp registredAt;

}
