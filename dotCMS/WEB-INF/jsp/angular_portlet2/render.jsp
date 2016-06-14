<h1>Hello word!, Portlet 2</h1>
<div id="showResultDiv"></div>

<script type="text/javascript">

    var resultTarget = dojo.byId("showResultDiv");

    dojo.xhrGet({
        url : "/api/jwt/poc2",
        handleAs : "json",
        sync: true,
        load : function(data) {
            console.debug(data.Success);
            resultTarget.innerHTML = data.Success;
        },
        error : function(error) {
            console.debug(error);
            resultTarget.innerHTML = "An unexpected error occurred: " + error.message + " -- [ " + error.responseText + " ]";
        }
    });
</script>