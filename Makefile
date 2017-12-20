start_dev:
	lein do clean, figwheel

compile_solidity:
	solcjs src/contracts/chat.sol \
		--overwrite --optimize --bin --abi \
		--combined-json bin,abi src/contracts/chat.sol \
		-o resources/public/contracts
	cat resources/public/contracts/*.abi | sed -e 's/\]\[/,/g' > resources/public/contracts/chat.json

build:
	lein cljsbuild once
	lein minify-assets
