import { json } from '@sveltejs/kit';
import type { RequestHandler } from './$types';
import axios from 'axios';

export const GET: RequestHandler = async ({ url }) => {
	let states = url.searchParams.getAll('states');
	let years = url.searchParams.getAll('years');
	let crimes = url.searchParams.getAll('crimes');
	let races = url.searchParams.getAll('races');
	let token = url.searchParams.getAll('token');

	let xmlData = `<?xml version="1.0" encoding="UTF-8"?>
 <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
     <soapenv:Header/>
     <soapenv:Body>
         <getSummaryRequest xmlns="http://test/summary">
            <states>${states[0] || ''}</states>
            <years>${years[0] || ''}</years>
            <crimes>${crimes[0] || ''}</crimes>
            <races>${races[0] || ''}</races>
         </getSummaryRequest>
     </soapenv:Body>
 </soapenv:Envelope>`;

	let response;
	// console.log(xmlData);

	try {
		// const response = await axios.post('http://localhost:8080/ws', xmlData, {
		// 	headers: {
		// 		'Content-Type': '*/*'
		// 	}
		// });
		const response = await fetch('http://localhost:8080/ws', {
			method: 'POST',
			headers: {
				'Content-Type': 'text/xml',
				'Access-Control-Allow-Origin': '*',
				Authorization: 'Bearer ' + token
			},
			body: xmlData
		});

		const data = await response.text();
		// console.log(response);
		// console.log(data);
		return json({ summary: data });
	} catch (error) {
		console.error('XML failed!', error);
	}

	return json({ err: 'Soap failed!' });
};
