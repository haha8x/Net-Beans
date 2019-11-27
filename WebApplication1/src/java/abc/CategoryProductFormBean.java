/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author duy.nn
 */
public class CategoryProductFormBean {

    private String selectedCategory;
    private List<SelectItem> categoryList;
    private List<Product> productList;

    public List<SelectItem> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<SelectItem> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public void loadProduct(ValueChangeEvent event) {
        try {
            Connection con = DBUtility.getConnection();
            PreparedStatement stm = con.prepareStatement("select p.ProductID, p.CategoryID, p.ProductName, p.Price, p.Description, c.CategoryName  from Products p inner join Categories c on p.CategoryID=c.CategoryID where p.CategoryID=?");
            String selectedCat = event.getNewValue().toString();
            stm.setString(1, selectedCat);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Product pro = new Product();
                pro.setProId(rs.getString("ProductID"));
                pro.setProName(rs.getString("ProductName"));
                pro.setPrice(rs.getInt("Price"));
                pro.setDescription(rs.getString("Description"));
                Category cat = new Category();
                cat.setCatName(rs.getString("CategoryName"));
                pro.setCategory(cat);
                productList.add(pro);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadCategory() {
        try {
            Connection con = DBUtility.getConnection();
            PreparedStatement stm = con.prepareStatement("select CategoryID, CategoryName from Categories");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                categoryList.add(new SelectItem(rs.getString("CategoryID"), rs.getString("CategoryName")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public CategoryProductFormBean() {
        categoryList = new ArrayList<SelectItem>();
        productList = new ArrayList<Product>();
        loadCategory();
    }
}
