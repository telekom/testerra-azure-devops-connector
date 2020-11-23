package eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper;

/**
 * Created on 20.11.2020
 *
 * @author mgn
 */
public class BasicObject {

    private Integer id;

    private String name;

    public BasicObject() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
