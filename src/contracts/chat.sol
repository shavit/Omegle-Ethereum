pragma solidity ^0.4.18;

contract Owned {
    address owner;

    function Owned() public {
    owner = msg.sender;
  }

  modifier onlyOwner {
    require(msg.sender == owner);
    _;
  }

  modifier onlyNotOwner {
    require(msg.sender != owner);
    _;
  }
}

contract Chat is Owned {
  string public username;
  uint256 private balance = 0;
  string private streamURL;
  uint constant tokenPrice = 0.001 ether;

  struct Streamer {
    uint id;
    bytes16 username;
    string url;
    uint created;
  }

  Streamer[] private streamers;

  event onBalanceUpdate(address author, uint256 balance);

  function setBalance(uint256 _balance) onlyOwner public {
      balance = _balance;

      onBalanceUpdate(msg.sender, balance);
  }

  function getBalance() view public returns (uint256){
    return balance;
  }

  // TODO: Remove this line before deployment
  /* function buyTokens() public payable onlyNotOwner { */
  function buyTokens(uint256 _amount) public payable {
    require(msg.value != 0);
    require(msg.value == (_amount * tokenPrice));

    if (msg.sender.send(msg.value) == true) {
      var newBalance = balance + _amount;
      balance = newBalance;
      onBalanceUpdate(msg.sender, newBalance);
    }
  }

  function addStreamer(uint _id, bytes16 _username, string _url, uint _created) public{
    var streamer = Streamer(_id, _username, _url, _created);
    streamers.push(streamer);
  }

  function getStreamers() view public returns(Streamer[]){
    return streamers;
  }

  function countStreamers() view public returns (uint){
    return streamers.length;
  }

  event onStreamUrlChange(address author, string url);

  function setStreamURL(string _url) onlyOwner public {
    streamURL = _url;

    onStreamUrlChange(msg.sender, _url);
  }

  function getStreamUrl() view public returns (string){
    return streamURL;
  }
}
