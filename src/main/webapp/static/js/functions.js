"use strict";

var current_page = 1;
var records_per_page = 13;

$(document).ready(function() {
	$("#searchForm").submit(function(event) {
		event.preventDefault();
		var searchTerm = $(this).find("input[name='s']").val();
		var url = window.location.href;
		var pageNumber = url.substr(url.lastIndexOf("/") + 1);
		if (pageNumber === "") {
			pageNumber = "1";
		}
		localStorage.searchTerm = searchTerm;
		changePage(pageNumber, searchTerm, false);
		setCurrencyDropDown("GBP");
	});
});

/**
 * Pagination: based on http://stackoverflow.com/a/25435422
 */
function changePage(page, keyWords, preloaded) {
	if (page < 1)
		page = 1;
	if (page > numPages())
		page = numPages();

	if (preloaded) {
		showPreloadedPage(page);
	} else {
		fillTable(page, keyWords);
	}

	updatePageCount(page);
	showPreviousButton(page);
	showNextButton(page);
}

/**
 * Returns count of result pages
 */
function numPages() {
	var stored = parseInt(localStorage.count / records_per_page + 1);
	return (stored < 4) ? stored : 4;
}

/**
 * Querying and filling result table
 */
function fillTable(pageNumber, keyWords) {
	$.ajax({
		type : "POST",
		url : 'tabledata/' + pageNumber + "/" + keyWords,
		success : function(data) {
			localStorage.count = data.itemCount;
			if (data.itemCount > 0) {
				localStorage
						.setItem('preloaded', JSON.stringify(data.itemList));
				var list = data.itemList;
				var end = records_per_page;
				if (end > list.length) {
					end = list.length;
				}
				var content = setTable(list, 0, end);
				$('#notificationArea').css('display', 'none');
				$('#myTableBody').val("");
				$('#myTableBody').html(content);
				updatePageCount(pageNumber);
				showNextButton(pageNumber);
			} else {
				$('#notificationArea').css('display', '');
				$('#notificationArea').text(
						"You have 0 results. Try to modify your query.");
				$('#myTableBody').html("");
			}
		}
	});
};

/**
 * Updating pagination element
 */
function updatePageCount(page) {
	var page_span = document.getElementById("page");
	page_span.innerHTML = page + "/" + numPages();
}

/**
 * Updating pagination element
 */
function showPreviousButton(page) {
	var btn_prev = document.getElementById("btn_prev");
	if (page == 1) {
		btn_prev.style.visibility = "hidden";
	} else {
		btn_prev.style.visibility = "visible";
	}
}

/**
 * Updating pagination element
 */
function showNextButton(page) {
	var btn_next = document.getElementById("btn_next");
	if (page == numPages()) {
		btn_next.style.visibility = "hidden";
	} else {
		btn_next.style.visibility = "visible";
	}
}

/**
 * Writing table's HTML
 */
function setTable(list, start, end) {
	var content = '';
	for (var i = start; i < end; i++) {
		content += '<tr id="' + i + '">';
		content += '<td><a href="' + list[i].detailPageURL
				+ '" target="_blank">' + list[i].title + '</a></td>';
		content += '<td style="display:none;" class="priceHidden">'
				+ (list[i].price / 100).toFixed(2) + '</td>';
		content += '<td style="display:none;" class="currencyHidden">'
				+ list[i].currency + '</td>';
		content += '<td class="price">' + (list[i].price / 100).toFixed(2)
				+ '</td>';
		content += '<td class="currency">' + list[i].currency + '</td>';
		content += '</tr>';
	}
	return content;
}

/**
 * Loading next page from browser's local storage
 */
function showPreloadedPage(page) {
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
	}
	var content = setTable(list, records_per_page, end);

	$('#myTableBody').val("");
	$('#myTableBody').html(content);

	var nextPage = parseInt(page) + 1;
	var searchTerm = localStorage.getItem('searchTerm');
	$.ajax({
		type : "POST",
		url : "tabledata/next/" + nextPage + "/" + searchTerm,
		success : function(data) {
			newList = newList.concat(data.itemList);
			localStorage.setItem('preloaded', JSON.stringify(newList));
		}
	});
};

/**
 * Loading previous page of results
 */
function prevPage() {
	if (current_page > 1) {
		current_page--;
		var searchTerm = localStorage.getItem('searchTerm');
		changePage(current_page, searchTerm, false);
	}
}

/**
 * Loading next page of results
 */
function nextPage() {
	if (current_page < numPages()) {
		current_page++;
		var searchTerm = localStorage.getItem('searchTerm');
		changePage(current_page, searchTerm, true);
	}
}

/**
 * Setting currency dropdown. Due to the Currencylayer's 1000 query limit new
 * results are loaded only once every 15 minutes
 */
function setCurrencyDropDown(currency) {
	var dateTime = Date.now();
	var timestamp = Math.floor(dateTime / 1000);
	var storedTimestamp = localStorage.getItem('timestamp');
	if (storedTimestamp != null && storedTimestamp + 900 < timestamp) {
		fetchAndStoreCurrencyData(currency);
	} else {
		var retrievedObject = localStorage.getItem('currencyData');
		if (retrievedObject == null) {
			fetchAndStoreCurrencyData(currency);
		} else {
			fetchLocalCurrency(retrievedObject, currency);
		}
	}
}

/**
 * Loading currencies from Currencylayer's web service
 */
function fetchAndStoreCurrencyData(currency) {
	$.ajax({
		type : "GET",
		url : "tabledata/currency/" + currency,
		success : function(data) {
			localStorage.setItem('currencyData', JSON.stringify(data));
			var dateTime = Date.now();
			var timestamp = Math.floor(dateTime / 1000);
			localStorage.setItem('timestamp', timestamp);
			setDropDown(data, currency);
		}
	});
}

/**
 * Loading currencies from local storage
 */
function fetchLocalCurrency(retrievedObject, currency) {
	var data = (JSON.parse(retrievedObject));
	setDropDown(data, currency);
}

/**
 * Setting currency dropdown
 */
function setDropDown(data, currency) {
	var selectEl = $('#currencyDropDown');
	selectEl.children().remove();
	var emptyEl;
	emptyEl = $('<option/>').attr('value', data[currency]).text(currency);
	selectEl.append(emptyEl);
	for ( var key in data) {
		var optionEl = $('<option/>').attr('value', data[key]).text(key);
		selectEl.append(optionEl);
	}
}

/**
 * Recalculating prices based on users choice
 */
$('#currencyDropDown').change(function() {
	var rate = this.value;
	var currency = $('#currencyDropDown option:selected').text();
	changePrice(rate);
	changeCurrency(currency);
});

/**
 * Changing prices one by one
 */
function changePrice(rate) {
	$('tr:not(:has(th))').each(function(i, row) {
		var $actualrow = $(row);
		var actualPrice = $actualrow.children(".priceHidden").first().text();
		var newPrice = (actualPrice * rate).toFixed(2);
		$actualrow.children(".price").first().text(newPrice);
	});
}

/**
 * Changing all currencies in results table at once
 */
function changeCurrency(currency) {
	$('td.currency').each(function() {
		this.innerHTML = currency;
	});
}
