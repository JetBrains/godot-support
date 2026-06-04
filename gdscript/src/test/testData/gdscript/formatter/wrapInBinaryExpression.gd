class Mock:
	func GetDefaults(_x): return {}


var nest = Mock.new()
var num_synapses = nest.GetDefaults("excitatory")["num_connections"] + nest.GetDefaults("inhibitory")["num_connections"]
