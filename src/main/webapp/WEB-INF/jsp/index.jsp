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
					<th>Price</th>
					<th>Currency</th>
				</tr>
				<tbody id="myTableBody">
					<tr>
						<td><a href="http://www.google.ee" target="_blank">nimetus</a></td>
						<td>21,00</td>
						<td>USD</td>
					</tr>
					<tr>
						<td>Centro comercial Moctezuma</td>
						<td>Francisco Chang</td>
						<td>Mexico</td>
					</tr>
					<tr>
						<td>Ernst Handel</td>
						<td>Roland Mendel</td>
						<td>Austria</td>
					</tr>
					<tr>
						<td>Island Trading</td>
						<td>Helen Bennett</td>
						<td>UK</td>
					</tr>
					<tr>
						<td>Laughing Bacchus Winecellars</td>
						<td>Yoshi Tannamuri</td>
						<td>Canada</td>
					</tr>
					<tr>
						<td>Magazzini Alimentari Riuniti</td>
						<td>Giovanni Rovelli</td>
						<td>Italy</td>
					</tr>
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
				<!--ul id="pagination-demo" class="pagination pagination-sm"></ul-->
			</div>
		</div>
		<div class="sidebar">
			<select>
				<option value="volvo">Volvo</option>
				<option value="saab">Saab</option>
				<option value="mercedes">Mercedes</option>
				<option value="audi">Audi</option>
			</select>
		</div>
	</div>
	<script type="text/javascript" src="static/js/jquery.min.js"></script>
	<!--script type="text/javascript"
		src="static/js/jquery.twbsPagination.min.js"></script-->
	<script type="text/javascript" src="static/js/functions.js"></script>
</body>
</html>