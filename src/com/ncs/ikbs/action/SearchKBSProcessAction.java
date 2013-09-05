/*
 * =========================================================================
 *  Program Name:  com.ncs.iforge.kbs.action
 *                    .SearchKBSProcessAction
 *
 *  Copyright ©2003-2004 NCS Pte. Ltd. All Rights Reserved
 *
 *  This software is confidential and proprietary to NCS Pte. Ltd. You shall
 *  use this software only in accordance with the terms of the license
 *  agreement you entered into with NCS.  No aspect or part or all of this
 *  software may be reproduced, modified or disclosed without full and
 *  direct written authorisation from NCS.
 *
 *  NCS SUPPLIES THIS SOFTWARE ON AN “AS IS” BASIS. NCS MAKES NO
 *  REPRESENTATIONS OR WARRANTIES, EITHER EXPRESSLY OR IMPLIEDLY, ABOUT THE
 *  SUITABILITY OR NON-INFRINGEMENT OF THE SOFTWARE. NCS SHALL NOT BE LIABLE
 *  FOR ANY LOSSES OR DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
 *  MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 *  Change Revision
 * -------------------------------------------------------------------------
 *  Version    Remarks
 *  v1.0       - Initial Release
 * =========================================================================
 */
package com.ncs.ikbs.action;

import com.ncs.iframe.commons.config.DefaultIFrameProperties;
import com.ncs.iframe.extensions.action.BaseFlowCommandAction;
import com.ncs.iframe.extensions.tagutil.PaginationUtil;
import com.ncs.iframe.extensions.tagutil.SortOrderUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class SearchKBSProcessAction extends BaseFlowCommandAction
{
  protected Map init(ActionMapping mapping, ActionForm form,
    HttpServletRequest request)
  {
    HashMap oMapInitValues = (HashMap) super.init(mapping, form, request);

    // For pagination
    oMapInitValues.put("currentPage",
      PaginationUtil.getCurrentPage(request, "1"));

    oMapInitValues.put("pageSize",
      DefaultIFrameProperties.getPropertyAsString(
        "extensions.taglib.pagination.pageSize", "10"));

    // For sortOrder
    oMapInitValues.put("sortValue", SortOrderUtil.getSortValue(request, "TITLE"));
    oMapInitValues.put("sortOrder",
      SortOrderUtil.getSortOrder(request, Boolean.valueOf("true")));

    return oMapInitValues;
  }
}
