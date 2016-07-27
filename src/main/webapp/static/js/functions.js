"use strict";

var current_page = 1;
var records_per_page = 13;

$(document).ready(function() {
	var url = window.location.href;
	var pageNumber = url.substr(url.lastIndexOf("/") + 1);
	if (pageNumber === "") {
		pageNumber = "1";
	}
	changePage(pageNumber);
	setCurrencyDropDown("GBP");
});

// based on http://stackoverflow.com/a/25435422
function changePage(page, preloaded) {
	var btn_next = document.getElementById("btn_next");
	var btn_prev = document.getElementById("btn_prev");
	var page_span = document.getElementById("page");

	if (page < 1)
		page = 1;
	if (page > numPages())
		page = numPages();

	if (preloaded) {
		showPreloadedPage(page);
	} else {
		fillTable(page);
	}

	page_span.innerHTML = page + "/" + numPages();

	if (page == 1) {
		btn_prev.style.visibility = "hidden";
	} else {
		btn_prev.style.visibility = "visible";
	}

	if (page == numPages()) {
		btn_next.style.visibility = "hidden";
	} else {
		btn_next.style.visibility = "visible";
	}
}

function numPages() {
	return parseInt(localStorage.count / records_per_page + 1);
}

function fillTable(pageNumber) {
	$
			.ajax({
				type : "GET",
				url : 'tabledata/' + pageNumber,
				success : function(data) {
					localStorage.count = data.itemCount;
					localStorage.setItem('preloaded', JSON
							.stringify(data.itemList));
					var list = data.itemList;
					$(function() {
						var content = '';
						var end = records_per_page;
						if (end > list.length) {
							end = list.length;
						}
						for (var i = 0; i < end; i++) {
							content += '<tr id="' + i + '">';
							content += '<td>' + list[i].title + '</td>';
							content += '<td style="display:none;" class="priceHidden">'
									+ (list[i].price).toFixed(2) + '</td>';
							content += '<td style="display:none;" class="currencyHidden">'
									+ list[i].currency + '</td>';
							content += '<td class="price">'
									+ (list[i].price).toFixed(2) + '</td>';
							content += '<td class="currency">'
									+ list[i].currency + '</td>';
							content += '</tr>';
						}
						$('#myTableBody').val("");
						$('#myTableBody').html(content);
					});
				}
			});
};

function showPreloadedPage(page) {
	$(function() {
		// Retrieve the object from storage
		var retrievedObject = localStorage.getItem('preloaded');
		var list = (JSON.parse(retrievedObject));
		var content = '';
		var end = records_per_page * 2;
		if (end > list.length) {
			end = list.length;
		}
		var newList = new Array();
		for (var i = records_per_page; i < end; i++) {
			newList.push(list[i]);
			content += '<tr id="' + i + '">';
			content += '<td>' + list[i].title + '</td>';
			content += '<td style="display:none;" class="priceHidden">'
					+ (list[i].price).toFixed(2) + '</td>';
			content += '<td style="display:none;" class="currencyHidden">'
					+ list[i].currency + '</td>';
			content += '<td class="price">' + (list[i].price).toFixed(2)
					+ '</td>';
			content += '<td class="currency">' + list[i].currency + '</td>';
			content += '</tr>';
		}
		$('#myTableBody').val("");
		$('#myTableBody').html(content);
		var nextPage = parseInt(page) + 1;
		$.ajax({
			url : 'tabledata/next/' + nextPage,
			success : function(data) {
				newList = newList.concat(data.itemList);
				localStorage.setItem('preloaded', JSON.stringify(newList));
			}
		});
	});
};

function prevPage() {
	if (current_page > 1) {
		current_page--;
		changePage(current_page, false);
	}
}

function nextPage() {
	if (current_page < numPages()) {
		current_page++;
		changePage(current_page, true);
	}
}

function setCurrencyDropDown(current) {
	$
			.ajax({
				type : "GET",
				url : "tabledata/currency/" + current,
				success : function(data) {
					var selectEl = $('#currencyDropDown');
					selectEl.children().remove();
					var emptyEl;
					emptyEl = $('<option/>').attr('value', data[current]).text(
							current);
					selectEl.append(emptyEl);
					for ( var key in data) {
						var optionEl = $('<option/>').attr('value', data[key])
								.text(key);
						selectEl.append(optionEl);
					}
				}
			});
}

$('#currencyDropDown').change(function() {
	var rate = this.value;
	var currency = $('#currencyDropDown option:selected').text();
	changePrice(rate);
	changeCurrency(currency);
});

function changePrice(rate) {
	$('tr:not(:has(th))').each(function(i, row) {
		var $actualrow = $(row);
		var actualPrice = $actualrow.children(".priceHidden").first().text();
		var newPrice = (actualPrice * rate).toFixed(2);
		$actualrow.children(".price").first().text(newPrice);
	});
}

function changeCurrency(currency) {
	$('td.currency').each(function() {
		this.innerHTML = currency;
	});
}
