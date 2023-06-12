<script lang="ts">
	import Chart from '../lib/Chart.svelte';

	import axios from 'axios';
	import RequestButton from '$lib/requestButton.svelte';
	import { ListBox, ListBoxItem, SlideToggle, Tab, TabGroup } from '@skeletonlabs/skeleton';
	let dataLoadingResponse = '';
	let currently_analyzed_data: any = [];
	let loading = false;
	let sorted_by = '?';
	let picked_year: string = '2013';
	let picked_crime: string = 'Murder';
	let tabSet: number = 0;
	let stretchGraphs = false;

	let defaultDataPerRace = {
		asian: {
			borderColor: 'rgba(255, 255, 0, .2)',
			backgroundColor: 'rgba(255, 255, 0, .5)',
			label: 'asian',
			data: []
		},
		black: {
			borderColor: 'rgba(0, 0, 0, .2)',
			backgroundColor: 'rgba(0, 0, 0, .5)',
			label: 'black',
			data: []
		},
		hispanic: {
			borderColor: 'rgba(100, 100, 200, .2)',
			backgroundColor: 'rgba(100, 100, 200, .5)',
			label: 'hispanic',
			data: []
		},
		nhwhite: {
			borderColor: 'rgba(255, 255, 255, .2)',
			backgroundColor: 'rgba(255, 255, 255, .5)',
			label: 'nhwhite',
			data: []
		},
		other: {
			borderColor: 'rgba(123, 123, 123, .2)',
			backgroundColor: 'rgba(123, 123, 123, .5)',
			label: 'other',
			data: []
		},
		nhopi: {
			borderColor: 'rgba(100, 200, 0, .2)',
			backgroundColor: 'rgba(100, 200, 0, .5)',
			label: 'nhopi',
			data: []
		}
	};

	let chartData = {
		datasets: [
			{
				borderColor: 'rgba(99,220,125, .2)',
				backgroundColor: 'rgba(99,220,125, .5)',
				label: 'V(node1)',
				data: [
					{
						x: 1,
						y: -1.711e-2
					},
					{
						x: 631,
						y: -3.196e1
					},
					{
						x: 794,
						y: -3.396e1
					},
					{
						x: 1000,
						y: -3.596e1
					}
				]
			}
		]
	};

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
		chartData.datasets = [];
		console.log(chartData.datasets);
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

			for (let j = 0; j < response.data[i].statistics[0]?.population.length; j++) {
				if (chartData.datasets.length == j) {
					chartData.datasets.push(
						JSON.parse(
							JSON.stringify(defaultDataPerRace[response.data[i].statistics[0].population[j].name])
						)
					);
				}
				chartData.datasets[j].data.push({
					x: response.data[i].statistics[0].population[j].population,
					y: response.data[i].statistics[0].crimes[0].rate
				});
			}
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

	<div style="margin-top: 12px; width: 800px;">
		<TabGroup>
			<Tab bind:group={tabSet} name="tab1" value={0}>table</Tab>
			<Tab bind:group={tabSet} name="tab2" value={1}>graphs</Tab>
			<Tab bind:group={tabSet} name="tab3" value={2}>asian</Tab>
			<Tab bind:group={tabSet} name="tab4" value={3}>black</Tab>
			<Tab bind:group={tabSet} name="tab5" value={4}>hispanic</Tab>
			<Tab bind:group={tabSet} name="tab6" value={5}>nhopi</Tab>
			<Tab bind:group={tabSet} name="tab7" value={6}>nhwhite</Tab>
			<Tab bind:group={tabSet} name="tab8" value={7}>other</Tab>

			<!-- Tab Panels --->
			<svelte:fragment slot="panel">
				{#if tabSet === 0}
					{#if currently_analyzed_data.length}
						<div
							style="background: rgb(31, 35, 48); margin: 12px 196px; padding: 12px; border-radius: 12px;"
						>
							all columns are sortable
							<br />
							rate = crimes of given type per 100000 population
							<br />
							races are expressed as % of state's total population
						</div>
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
															<abbr
																title={currently_analyzed_data[0]?.statistics[0]?.population[i]
																	?.abbr}
															>
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
														<td
															>{Math.round(row?.statistics[0]?.population[i]?.population * 100) /
																100}</td
														>
													{/if}
												{/each}
											</tr>
										{/if}
									{/each}
								</tbody>
							</table>
						</div>
					{/if}
				{:else if tabSet === 1}
					<Chart {chartData} {stretchGraphs} />
					<SlideToggle name="slide" disabled bind:checked={stretchGraphs}
						>stretch diagrams</SlideToggle
					>
				{:else if tabSet === 2}
					<Chart {stretchGraphs} chartData={{ datasets: [chartData.datasets[0]] }} />
					<SlideToggle name="slide" bind:checked={stretchGraphs}>stretch diagrams</SlideToggle>
					<!-- <Chart chartData={{ datasets: [chartData.datasets[0]] }} /> -->
				{:else if tabSet === 3}
					<Chart {stretchGraphs} chartData={{ datasets: [chartData.datasets[1]] }} />
					<SlideToggle name="slide" bind:checked={stretchGraphs}>stretch diagrams</SlideToggle>
					<!-- <Chart chartData={{ datasets: [chartData.datasets[1]] }} /> -->
				{:else if tabSet === 4}
					<Chart {stretchGraphs} chartData={{ datasets: [chartData.datasets[2]] }} />
					<SlideToggle name="slide" bind:checked={stretchGraphs}>stretch diagrams</SlideToggle>
					<!-- <Chart chartData={{ datasets: [chartData.datasets[2]] }} /> -->
				{:else if tabSet === 5}
					<Chart {stretchGraphs} chartData={{ datasets: [chartData.datasets[3]] }} />
					<SlideToggle name="slide" bind:checked={stretchGraphs}>stretch diagrams</SlideToggle>
					<!-- <Chart chartData={{ datasets: [chartData.datasets[3]] }} /> -->
				{:else if tabSet === 6}
					<Chart {stretchGraphs} chartData={{ datasets: [chartData.datasets[4]] }} />
					<SlideToggle name="slide" bind:checked={stretchGraphs}>stretch diagrams</SlideToggle>
					<!-- <Chart chartData={{ datasets: [chartData.datasets[4]] }} /> -->
				{:else if tabSet === 7}
					<Chart {stretchGraphs} chartData={{ datasets: [chartData.datasets[5]] }} />
					<SlideToggle name="slide" bind:checked={stretchGraphs}>stretch diagrams</SlideToggle>
					<!-- <Chart chartData={{ datasets: [chartData.datasets[5]] }} /> -->
				{/if}
			</svelte:fragment>
		</TabGroup>
	</div>
	<div>
		{#if dataLoadingResponse}
			dataLoadingResponse
		{/if}
	</div>
</div>
