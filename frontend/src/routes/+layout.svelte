<script lang="ts">
	// The ordering of these imports is critical to your app working properly
	import '@skeletonlabs/skeleton/themes/theme-crimson.css';
	// If you have source.organizeImports set to true in VSCode, then it will auto change this ordering
	import '@skeletonlabs/skeleton/styles/skeleton.css';
	// Most of your app wide CSS should be put in this file
	import '../app.postcss';
	import { AppShell, AppBar } from '@skeletonlabs/skeleton';
	import { browser } from '$app/environment';
	import { data } from '../store';
	import { goto } from '$app/navigation';

	async function logOut() {
		$data.token = '';
		localStorage.setItem('token', $data.token);
		goto('/login');
	}
</script>

<!-- App Shell -->
<AppShell>
	<svelte:fragment slot="header">
		<!-- App Bar -->
		<AppBar>
			<svelte:fragment slot="lead">
				<a href="/">
					<strong class="text-xl uppercase">Analiza przestępczości w stanach zjednoczonych</strong>
				</a>
			</svelte:fragment>
			<svelte:fragment slot="trail">
				{#if browser && !$data.token}
					<a class="btn btn-sm variant-ghost-surface" href="/login"> Log in </a>
					<a class="btn btn-sm variant-ghost-surface" href="/register"> Register </a>
				{:else}
					<button class="btn btn-sm variant-ghost-surface" on:click={logOut}> Log out </button>
				{/if}
			</svelte:fragment>
		</AppBar>
	</svelte:fragment>
	<!-- Page Route Content -->
	<slot />
</AppShell>
