/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abc;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author duy.nn
 */
public class Category  implements Serializable{

    private String catId;
    private String catName;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public Category(String catId, String catName) {
        this.catId = catId;
        this.catName = catName;
    }

    public Category() {
    }
}
