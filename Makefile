start_dev:
	lein do clean, figwheel

compile_solidity:
	solcjs src/contracts/chat.sol \
		--overwrite --optimize --bin --abi \
		--combined-json bin,abi src/contracts/chat.sol \
		-o target/contracts 

build:
	lein cljsbuild once
	lein minify-assets
