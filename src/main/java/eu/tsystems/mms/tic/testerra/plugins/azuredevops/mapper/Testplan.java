package eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper;

/**
 * Created on 20.11.2020
 *
 * @author mgn
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Testplan extends BasicObject {

    public Testplan() {

    }

    public Testplan(int id) {
        this.setId(id);
    }

}
