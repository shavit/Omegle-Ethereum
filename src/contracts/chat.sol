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
}

contract Chat is Owned {
  string public username;
  int private balance = 0;
  string private streamURL;

  struct Streamer {
    uint id;
    bytes16 username;
    string url;
    uint created;
  }

  Streamer[] private streamers;

  event onBalanceUpdate(address author, int balance);

  function setBalance(int _balance) onlyOwner public {
      balance = _balance;

      onBalanceUpdate(msg.sender, balance);
  }

  function getBalance() view public returns (int){
    return balance;
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
}
