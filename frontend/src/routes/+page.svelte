<script lang="ts">
	import axios from 'axios';
	import RequestButton from '$lib/requestButton.svelte';
	import { ListBox, ListBoxItem } from '@skeletonlabs/skeleton';
	let dataLoadingResponse = '';
	let currently_analyzed_data: any = [];
	let loading = false;
	let sorted_by = '?';
	let picked_year: string = '2013';
	let picked_crime: string = 'Murder';

	$: if ((picked_crime, picked_year)) {
		getDataFromDB();
	}

	function reorder_by_rate() {
		loading = true;
		console.log(currently_analyzed_data);
		if (sorted_by != 'rate_asc') {
			currently_analyzed_data = currently_analyzed_data.sort((a, b) => {
				return a?.statistics[0]?.crimes[0]?.rate - 0 > b?.statistics[0]?.crimes[0]?.rate - 0
					? 1
					: -1;
			});
			sorted_by = 'rate_asc';
		} else {
			currently_analyzed_data = currently_analyzed_data.sort((a, b) => {
				return a?.statistics[0]?.crimes[0]?.rate - 0 < b?.statistics[0]?.crimes[0]?.rate - 0
					? 1
					: -1;
			});
			sorted_by = 'rate_desc';
		}
		loading = false;
	}

	function reorder_by_state() {
		loading = true;
		console.log(currently_analyzed_data);
		if (sorted_by != 'state_asc') {
			currently_analyzed_data = currently_analyzed_data.sort((a, b) => {
				return a?.name > b?.name ? 1 : -1;
			});
			sorted_by = 'state_asc';
		} else {
			currently_analyzed_data = currently_analyzed_data.sort((a, b) => {
				return a?.name < b?.name ? 1 : -1;
			});
			sorted_by = 'state_desc';
		}
		loading = false;
	}

	function reorder_by_race(race) {
		loading = true;
		console.log(currently_analyzed_data);
		if (sorted_by != `${race}_asc`) {
			currently_analyzed_data = currently_analyzed_data.sort((a, b) => {
				// currently_analyzed_data[0]?.statistics[0]?.population[i]?.name
				return a?.statistics?.[0]?.population[race]?.population - 0 >
					b?.statistics?.[0]?.population[race]?.population - 0
					? 1
					: -1;
				// return a?.name > b?.name ? 1 : -1;
			});
			sorted_by = `${race}_asc`;
		} else {
			currently_analyzed_data = currently_analyzed_data.sort((a, b) => {
				return a?.statistics?.[0]?.population[race]?.population - 0 <
					b?.statistics?.[0]?.population[race]?.population - 0
					? 1
					: -1;
			});
			sorted_by = `${race}_desc`;
		}
		loading = false;
	}

	async function getDataFromDB() {
		loading = true;
		let response = await axios.get(
			`http://localhost:8080/graphs?years=${picked_year}&crimes=${picked_crime}`,
			{
				headers: {
					'Content-Type': 'application/json',
					'Access-Control-Allow-Origin': '*'
				}
			}
		);
		loading = false;
		console.log(response);
		for (let i = 0; i < response.data.length; i++) {
			// console.log(response.data[i]?.statistics[0]);
			response.data = response.data.filter(
				(elem) => elem.name != 'United States' && elem.name != 'State'
			);
			for (let j = 0; j < response.data[i].statistics[0]?.population.length; j++) {
				let n = response.data[i].statistics[0].population[j].name;
				let abbr = '';
				switch (n) {
					case 'asian_est':
						n = 'asian';
						break;
					case 'black_est':
						n = 'black';
						break;
					case 'hisp_est':
						n = 'hispanic';
						break;
					case 'nhwhite_est':
						n = 'nhwhite';
						abbr = 'non-hispanic white';
						break;
					case 'other_est':
						n = 'other';
						abbr = 'everyone else (eg. other races and mixed people)';
						break;
					case 'nhopi_est':
						n = 'nhopi';
						abbr = 'Native Hawaiians and other Pacific Islanders';
						break;
					default:
						break;
				}
				response.data[i].statistics[0].population[j].name = n;
				response.data[i].statistics[0].population[j].abbr = abbr;
			}
			response.data[i].statistics[0]?.population.sort((a, b) => {
				if (a.name > b.name) {
					return 1;
				} else {
					return -1;
				}
			});
		}
		currently_analyzed_data = response.data;
	}
</script>

<div class="container h-full mx-auto flex justify-start items-center flex-col p-16">
	<RequestButton
		text="load data to DB"
		to_be_sorted={false}
		bind:responseData={dataLoadingResponse}
		awaitedFunction={() => {
			return axios.post('http://localhost:8080/data/loadData', {
				headers: {
					'Content-Type': 'application/json',
					'Access-Control-Allow-Origin': '*'
				}
			});
		}}
	/>

	<ListBox on:click={getDataFromDB}>
		<div style="display: flex; flex-direction: row;">
			{#each { length: 9 } as _, i}
				<ListBoxItem bind:group={picked_year} name="medium" value={`${i + 2005}`}
					>{i + 2005}</ListBoxItem
				>
			{/each}
		</div>
	</ListBox>

	<ListBox>
		<div style="display: flex; flex-direction: row;">
			{#each ['Murder', 'Assault', 'Rape', 'Larceny', 'Burglary', 'Robbery'] as crime}
				<ListBoxItem bind:group={picked_crime} disabled={loading} name="medium" value={crime}
					>{crime}</ListBoxItem
				>
			{/each}
		</div>
	</ListBox>

	<!-- <RequestButton
		text="get data from DB"
		bind:responseData={currently_analyzed_data}
		awaitedFunction={getDataFromDB}
	/> -->

	{#if currently_analyzed_data.length}
		<div class="table-container" style="margin-top: 8px;">
			<table class="table table-hover">
				<thead>
					<tr>
						<th><button disabled={loading} on:click={reorder_by_state}>state</button></th>
						<th
							><button disabled={loading} on:click={reorder_by_rate}
								><abbr title="per 100000pop per year">rate</abbr></button
							></th
						>
						{#if currently_analyzed_data.length > 1}
							{#each { length: currently_analyzed_data[0]?.statistics[0]?.population?.length } as _, i}
								<th>
									<button
										disabled={loading}
										on:click={() => {
											console.log(i);
											reorder_by_race(i);
										}}
									>
										{#if currently_analyzed_data[0]?.statistics[0]?.population[i]?.abbr}
											<abbr title={currently_analyzed_data[0]?.statistics[0]?.population[i]?.abbr}>
												{currently_analyzed_data[0]?.statistics[0]?.population[i]?.name}
											</abbr>
										{:else}
											{currently_analyzed_data[0]?.statistics[0]?.population[i]?.name}
										{/if}
									</button>
								</th>
							{/each}
						{/if}
					</tr>
				</thead>
				<tbody>
					{#each currently_analyzed_data as row, i}
						{#if row.name != 'State' && row.name != 'United States'}
							<tr>
								<td>{row.name}</td>
								<td>{row.statistics[0].crimes[0].rate}</td>
								{#each { length: currently_analyzed_data[0]?.statistics[0]?.population?.length } as _, i}
									{#if row?.statistics[0]?.population[i]?.name == 'other/mixed'}
										<td
											>{Math.round(
												(100 -
													row?.statistics[0]?.population[0]?.population -
													row?.statistics[0]?.population[1]?.population -
													row?.statistics[0]?.population[2]?.population -
													row?.statistics[0]?.population[3]?.population -
													row?.statistics[0]?.population[4]?.population) *
													100
											) / 100}</td
										>
									{:else}
										<td>{Math.round(row?.statistics[0]?.population[i]?.population * 100) / 100}</td>
									{/if}
								{/each}
							</tr>
						{/if}
					{/each}
				</tbody>
			</table>
		</div>
	{/if}

	<div>
		{#if dataLoadingResponse}
			dataLoadingResponse
		{/if}
	</div>
</div>
