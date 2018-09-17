package com.lucky.search.solr.handler;

import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.handler.component.SearchHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public class SearchByIdHandler extends SearchHandler {

  public static final String PP_ID = "pp_id";
  public static final String LOT_ID = "lot_id";
  public static final String GRP_ID = "grp_id";
  public static final String LOT_NUM = "lotnum";
  private static final String ID = "id";
  private static final String COLON = ":";

  @Override
  public void handleRequestBody(
      SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
    String query = createQueryFromParams(req.getParams());

    ModifiableSolrParams params = new ModifiableSolrParams(req.getParams());
    params.add(CommonParams.Q, query);
    super.handleRequestBody(req, rsp);
  }

  private static String createQueryFromParams(SolrParams params) {
    String ppId = params.get(PP_ID);
    String lotId = params.get(LOT_ID);
    if (ppId != null) {
      return createPpQuery(ppId);
    } else if (lotId != null) {
      return createLotQuery(lotId);
    } else {
      String id = params.get(ID);
      return createPpQuery(id) + " OR " + createLotQuery(id);
    }
  }

  private static String createPpQuery(String id) {
    if (id.contains(",")) {
      String query = id.replaceAll(",", " OR ");
      return GRP_ID + COLON + "(" + query + ")";
    }
    return GRP_ID + COLON + id;
  }

  private static String createLotQuery(String id) {
    return "{!parent which=scope:pp}" + LOT_NUM + COLON + id;
  }

  @Override
  public String getDescription() {
    return "Searches products by pp or lot id";
  }



}
