package dps.simplemailing.admin.views;

import java.util.LinkedHashMap;
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

    public void setPage(int page) {
        this.page = page;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Map<Integer,String> getAllLinks()
    {
        Map<Integer,String> links = new LinkedHashMap<>();
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
