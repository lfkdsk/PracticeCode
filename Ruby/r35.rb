def gold_room
  puts "This room is full of gold . How much do youy take"
  print "> "

  choice = $stdin.gets.chomp

  if choice.include?("0") || choice.include?("1")
    how_much = choice.to_i
  else
    dead("Man,learn to type a number")
  end
end

def bear_room
  puts " There is a bear here"
  puts "The bear has a bunch of honey"
  puts "The fat bear is in front of another door."
  puts "How are going to move the beer?"
  bear_moved = false

  while true
    print "> "
    choice = $stdin.gets.chomp

    if choice == "take honey"
      dead("The bear looks at you then slaps your face off.")
    elsif choice == "taunt bear " && !bear_moved
      dead("The bear gets pissed off and chews your leg off")
      bear_moved = true
    elsif choice == "taunt bear" && bear_moved
      dead("The bear gets pissed off and chews your leg off")
    elsif choice == "open door" && bear_moved
      gold_room
    else
      puts "I got on idea what that means"
    end
  end
end

def cthulhu_room
  puts "Here you see the great evil Cthulhu"
  puts "He, it ,whatever stares at you and you go insane"
  puts "Do you flee for your life or eat your head?"

  print ">"

  choice = $stdin.gets.chomp

  if choice.include? "flee"
    start
  elsif choice.include? "head"
    dead("Well that was tasty")
  else
    cthulhu_room
  end
end

def dead(why)
  puts why,"Good Job"
  exit(0)
end

def start
  puts "You are in a dark room"
  puts "There is a door to your right and left"
  puts "Which one do you take?"

  print "> "
  choice = $stdin.gets.chomp

  if choice == "left"
    bear_room
  elsif choice == "right"
    cthulhu_room
  else
    dead("You stumble around the room util you starve.")
  end
end

start
