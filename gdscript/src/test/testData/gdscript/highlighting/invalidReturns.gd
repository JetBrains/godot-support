class A:
	pass

class B:
	pass
	
class C:
	pass
	
func rpc_response(
	_peer_to : A,
	_bindings : Array = [])-> B:
	return <error descr="Returns a type [C] which do not match function's [B]">C</error>