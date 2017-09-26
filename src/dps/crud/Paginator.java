/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dps.crud;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ferenci84
 */
public class Paginator {
    private int page;
    private int resultsPerPage;
    private Long count;
    private String prefix;

    public Paginator(int page, int resultsPerPage, Long count, String prefix) {
        this.page = page;
        this.resultsPerPage = resultsPerPage;
        this.count = count;
        this.prefix = prefix;
    }
    
    public int getPages()
    {
        return (int) ((count-1)/resultsPerPage+1);
    }
    
    public Boolean getHasNext()
    {
        return getPages() > page;
    }
    
    public Boolean getHasPrevious()
    {
        return page >= 1;
    }
    
    public String getNextLink()
    {
        return prefix+(page+1);
    }
    
    public Map<Integer,String> getAllLinks()
    {
        Map<Integer,String> links = new LinkedHashMap<Integer,String>();
        int pages = getPages();
        for(int i = 0; i < pages; i++) {
            links.put(i+1,prefix+i);
        }
        return links;
    }
    
    public String getPreviousLink()
    {
        return prefix+(page-1);
    }

}
