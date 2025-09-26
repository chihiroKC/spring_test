        function toggleForm(type,isNew) {
		const formId = (type === 'address') ? 'newAddressForm' : 'newCardForm';
		
		const formElement = document.getElementById(formId);
		if (formElement) {
			formElement.style.display = isNew?'block' : 'none';
		}
	}
	
	document.addEventListener('DOMContentLoaded', (event)=>{
		const newAddressRadio = document.getElementById('newAddressRadio');
		
		if(newAddressRadio) {
			const isChecked = newAddressRadio.checked;
			toggleForm('address', isChecked);
		}
		
		
	const newCardRadio = document.getElementById('newCardRadio');
	
	     if (newCardRadio) {
		     const isNewCard = newCardRadio.checked;
		     toggleForm('card', isNewCard);
	     }
	});
