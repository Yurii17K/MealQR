'use strict';

var MealQrCards = document.querySelector('.MealQr');
var AllCards = document.querySelectorAll('.MealQrCard');
var No = document.getElementById('No');
var Yes = document.getElementById('Yes');

function Cards(card, index)
{
  var NewCards = document.querySelectorAll('.MealQrCard:not(.removed)');

  NewCards.forEach(function (card, index)
  {
    card.style.zIndex = AllCards.length - index;
    card.style.transform = 'scale(' + (30 - index) / 30 + ') translateY(-' + 30 * index + 'px)';
    card.style.opacity = (10 - index) / 10;
  });
  
  MealQrCards.classList.add('loaded');
}

Cards();

AllCards.forEach(function (Element)
{
  var hammertime = new Hammer(Element);

  hammertime.on('pan', function (event)
  {
    Element.classList.add('moving');
  });

  hammertime.on('pan', function (event)
  {
    if (event.deltaX === 0 || (event.center.x === 0 && event.center.y === 0))
	{
		return;
	}

    MealQrCards.classList.toggle('MealQr_Yes', event.deltaX > 0);
    MealQrCards.classList.toggle('MealQr_No', event.deltaX < 0);

    var X = event.deltaX;
    var Y = event.deltaY;
    var R = (X * 0.1) * (Y / 50);

    event.target.style.transform = 'translate(' + X + 'px, ' + Y + 'px) rotate(' + R + 'deg)';
  });

  hammertime.on('panend', function (event)
  {
    Element.classList.remove('moving');
    MealQrCards.classList.remove('MealQr_Yes');
    MealQrCards.classList.remove('MealQr_No');

    var moveOutWidth = document.body.clientWidth;
    var keep = Math.abs(event.deltaX) < 80 || Math.abs(event.velocityX) < 0.5;

    event.target.classList.toggle('removed', !keep);

    if (keep)
	{
      event.target.style.transform = '';
    }
	else
	{
      var MaxX = Math.max(Math.abs(event.velocityX) * moveOutWidth, moveOutWidth);
	  if(event.deltaX > 0)
	  {
		 var toX = MaxX;
	  }
	  else
	  {
		 var toX = -MaxX;
	  }

      var MaxY = Math.abs(event.velocityY) * moveOutWidth;
	   if(event.deltaY > 0)
	  {
		 var toY = MaxY;
	  }
	  else
	  {
		 var toY = -MaxY;
	  }
	  
      var X = event.deltaX;
      var Y = event.deltaY;
      var R = (X * 0.1) * (Y / 50);

      event.target.style.transform = 'translate(' + toX + 'px, ' + (toY + Y) + 'px) rotate(' + R + 'deg)';
      Cards();
    }
  });
});

function createButtonListener(Yes)
{
  return function (event)
  {
    var cards = document.querySelectorAll('.MealQrCard:not(.removed)');
    var moveOutWidth = document.body.clientWidth * 1.5;

    if (!cards.length)
	{
		return false;
	}

    var card = cards[0];

    card.classList.add('removed');

    if (Yes)
	{
      card.style.transform = 'translate(' + moveOutWidth + 'px, -100px) rotate(-30deg)';
    }
	else
	{
      card.style.transform = 'translate(-' + moveOutWidth + 'px, -100px) rotate(30deg)';
    }

    Cards();

    event.preventDefault();
  };
}

var NoListener = createButtonListener(false);
var YesListener = createButtonListener(true);

No.addEventListener('click', NoListener);
Yes.addEventListener('click', YesListener);