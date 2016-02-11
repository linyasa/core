<%@page import="com.dotmarketing.portlets.languagesmanager.model.Language"%>
<%@page import="java.util.*" %>
<%@page import="com.dotmarketing.beans.*" %>
<%@page import="com.dotmarketing.util.*" %>
<%@page import="com.dotmarketing.business.IdentifierFactory" %>
<%@page import="com.liferay.portal.language.LanguageUtil"%>
<%@page import="com.dotmarketing.factories.WebAssetFactory" %>
<%@page import="com.dotcms.publisher.assets.bean.HistoricalPushedAsset" %>
<%@page import="com.dotmarketing.portlets.htmlpages.model.HTMLPage" %>
<%@page import="com.dotmarketing.portlets.folders.model.Folder" %>
<%@page import="com.dotmarketing.portlets.structure.model.Structure" %>
<%@page import="com.dotcms.publisher.bundle.bean.Bundle" %>
<%@page import="com.liferay.portal.model.User"%>

<%
	Object assetObject = request.getAttribute(com.dotmarketing.util.WebKeys.PERMISSIONABLE_EDIT);
    String assetId = assetObject==null ? "" :  assetObject instanceof Folder ? ((Folder)assetObject).getInode() :
    					assetObject instanceof Structure ? ((Structure)assetObject).getInode() :
                        (assetObject instanceof Inode ? ((Inode)assetObject).getIdentifier() :
                            (assetObject instanceof Contentlet ? ((Contentlet)assetObject).getIdentifier() : ""));

	List<HistoricalPushedAsset> historicalPushedAssets = assetObject!=null ? APILocator.getHistoricalPushedAssetsAPI().getPushedAssets(assetId) : new ArrayList<HistoricalPushedAsset>();

%>

<script>

function deletePushHistory() {

	var xhrArgs = {
		url : '/api/bundle/deletepushhistory/assetid/<%=(assetObject!=null ? assetId : "")%>',
		handleAs : "json",
		sync: false,
		load : function(data) {

		},
		error : function(error) {
			targetNode.innerHTML = "An unexpected error occurred: " + error;
		}
	}

	var deferred = dojo.xhrGet(xhrArgs);
	document.location.reload(true);
}
</script>

<%@page import="com.dotmarketing.business.APILocator"%>
<%@page import="com.dotmarketing.business.IdentifierCache"%>
<%@page import="com.dotmarketing.portlets.contentlet.model.Contentlet"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.dotmarketing.portlets.contentlet.business.DotContentletStateException"%>
<%@page import="com.dotmarketing.business.Versionable"%>

<%@page import="com.dotmarketing.business.PermissionAPI"%>
<%@page import="com.dotmarketing.business.Permissionable"%>
<%@page import="com.dotcms.publisher.environment.bean.Environment"%>


<div class="yui-g portlet-toolbar" style="padding-top: 10px">
	<div class="yui-u first" style="font-weight: bold; padding-left: 15px">
		<%= LanguageUtil.get(pageContext, "publisher_push_history") %>
	</div>

	<div class="yui-u" style="text-align:right;">
		<button dojoType="dijit.form.Button" onClick="deletePushHistory();" iconClass="deleteIcon" disabled='<%=historicalPushedAssets.isEmpty()%>'>
			<%= LanguageUtil.get(pageContext, "publisher_delete_asset_history") %>
		</button>
	</div>
</div>

<table class="listingTable">
	<tr>
		<th width="45%"><%= LanguageUtil.get(pageContext, "publisher_pushed_by") %></th>
		<th width="45%"><%= LanguageUtil.get(pageContext, "publisher_push_date") %></th>
		<th width="20%"><%= LanguageUtil.get(pageContext, "publisher_Environment") %></th>
		<th width="10%" nowrap><%= LanguageUtil.get(pageContext, "publisher_Identifier") %></th>
	</tr>
<%
	for(HistoricalPushedAsset historicalPushedAsset : historicalPushedAssets) {

		Environment env = APILocator.getEnvironmentAPI().findEnvironmentById(historicalPushedAsset.getEnvironmentId());
		Bundle bundle = APILocator.getBundleAPI().getBundleById(historicalPushedAsset.getBundleId());
		User owner = APILocator.getUserAPI().loadUserById(bundle.getOwner());


%>
	<tr  >
		<td><%= owner.getFullName() %></td>
		<td><%= UtilMethods.dateToHTMLDate(historicalPushedAsset.getPushDate()) %> - <%= UtilMethods.dateToHTMLTime(historicalPushedAsset.getPushDate()) %></td>
		 <td><%= (env != null) ? env.getName() : LanguageUtil.get(pageContext, "deleted") %></td>
		 <td nowrap="nowrap">
			<%= historicalPushedAsset.getBundleId() %>
		</td>
	</tr>
<% } if (historicalPushedAsset.size() == 0) { %>
	<tr>
		<td colspan="5">
			<div class="noResultsMessage"><%= LanguageUtil.get(pageContext, "publisher_status_no_push_history") %></div>
		</td>
	</tr>
<% } %>

</table>
