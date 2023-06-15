import { writable } from 'svelte/store';
import { browser } from '$app/environment';

export const data = writable({
	token: (browser && window.localStorage.getItem('token')) || ''
});
