import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;

public class Person {
  private String name;
  private String email;
  private int id;


  public Person(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public String getName(){
    return this.name;
  }

  public String getEmail(){
    return this.email;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object otherPerson) {
    if(!(otherPerson instanceof Person)) {
      return false;
    } else {
      Person newPerson = (Person) otherPerson;
      return this.getName().equals(newPerson.getName())
      && this.getEmail().equals(newPerson.getEmail());
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO persons(name, email) VALUES (:name, :email)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("email", this.email)
      .executeUpdate()
      .getKey();
    }
  }

  public static List<Person> all() {
    String sql = "SELECT * FROM persons";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Person.class);
    }
  }

  public static Person find(int id){
    String sqlCommand = "SELECT * FROM persons WHERE id=:id;";
    try(Connection con=DB.sql2o.open()){
      Person result = con.createQuery(sqlCommand)
        .addParameter("id", id)
        .executeAndFetchFirst(Person.class);
      return result;
    }
  }

}
