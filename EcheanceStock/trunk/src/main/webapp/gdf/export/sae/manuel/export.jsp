<html>
	<head>
		<title>Page Export SAE</title>		
	</head>
	<body>	
		<jsp:useBean id="user" scope="session" class="fr.extelia.gdf.sae.scheduler.bean.JspBean" />				
		<%if("true".equals(user.export(request.getParameter("username"), request.getParameter("password")))){%>  
		     <script LANGUAGE="JavaScript">    
				<!--
				alert ("Vacation d'exprot SAE manuelle lancée ...");
		       	document.location.href='/AppSAE';	               
		        //-->
	        </script>	 
	    <%}else if ("false".equals(user.export(request.getParameter("username"), request.getParameter("password")))){%>
			<script LANGUAGE="JavaScript">    
				<!--
				alert ("Mot de passe ou login invalide.");
	            document.location.href='/AppSAE';	               
	        	//-->
       		</script>	      
	    <%}else if ("Exception".equals(user.export(request.getParameter("username"), request.getParameter("password")))){%>
			<script LANGUAGE="JavaScript">    
				<!--
				alert ("Une Exception s'est produite lors de l'export.");
	            document.location.href='/AppSAE';	               
	        	//-->
       		</script>
	    <%}%>         	
	</body>
</html>