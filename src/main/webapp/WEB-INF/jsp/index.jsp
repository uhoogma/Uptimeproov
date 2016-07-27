<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<title>Amazon search</title>
<link rel="stylesheet" href="static/css/styles.css">
<link rel="stylesheet" href="static/css/bootstrap.min.css">
</head>
<body>
	<div class="wrapper">
		<div class="header">
			<h2>Amazon search</h2>
			<input class="inputStyle" type="text" name="search" /> <a href="#"
				class="button">Search</a>
		</div>
		<div class="content">
			<table>
				<tr>
					<th>Product</th>
					<th style="display:none;"></th>
					<th style="display:none;"></th>
					<th>Price</th>
					<th>Currency</th>
				</tr>
				<tbody id="myTableBody">
				</tbody>
			</table>
			<div class="center">
				<div id="listingTable"></div>
				<ul id="pagination-demo2" class="pagination pagination-sm">
					<li><a class="pagination pagination-sm"
						href="javascript:prevPage()" id="btn_prev">Prev</a></li>
					<li><a href="." style="margin-top: 20px; height: 30px;">page:
							<span id="page"></span>
					</a></li>
					<li><a class="pagination pagination-sm"
						href="javascript:nextPage()" id="btn_next">Next</a></li>
				</ul>
			</div>
		</div>
		<div class="sidebar">
			<select class="form-control" id="currencyDropDown">
			</select>
		</div>
	</div>
	<script type="text/javascript" src="static/js/jquery.min.js"></script>
	<script type="text/javascript" src="static/js/functions.js"></script>
</body>
</html>