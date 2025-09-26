
document.addEventListener('DOMContentLoaded', (event) => {
	const body = document.querySelector('body');
	
	const price = body.dataset.price;
	const taxPrice = body.dataset.taxPrice;
	const stock = body.dataset.stock;
	
	console.log("価格：", price);
	onsole.log("税込価格：", taxPrice);
	onsole.log("在庫数：", stock);
})