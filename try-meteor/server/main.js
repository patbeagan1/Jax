import { Meteor } from 'meteor/meteor';

Meteor.startup(() => {
  // code to run on server at startup
});

Meteor.startup(function () {
    console.log('starting up');
	var data = Assets.getText('HelloWorld.java');
	console.log(data);
});
