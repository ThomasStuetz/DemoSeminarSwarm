package at.htl.demoseminarswarm.model;


import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder={"id", "gender", "firstname", "lastname", "email", "country", "age", "registered"})
public class Person {

    int id;
    Gender gender;
    String firstname, lastname, email, country;
    int age;
    boolean registered;

    //region Constructor
    public Person(@NotNull int id,
                  @NotNull Gender gender,
                  @NotNull String firstname,
                  @NotNull String lastname,
                  String email,
                  String country,
                  int age,
                  boolean registered) {
        this.id = id;
        this.gender = gender;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.country = country;
        this.age = age;
        this.registered = registered;
    }

    public Person() {
    }

    //endregion

    //region Getter and Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
    //endregion

    @Override
    public String toString() {
        return "{" +
                "id : " + id +
                ", gender : '" + gender + '\'' +
                ", firstname : '" + firstname + '\'' +
                ", lastname : '" + lastname + '\'' +
                ", email : '" + email + '\'' +
                ", country : '" + country + '\'' +
                ", age : " + age +
                ", registered : " + registered +
                '}';
    }
}
