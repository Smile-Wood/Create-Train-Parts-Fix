import os
import json

# Folder containing the model files
folder_path = r'C:\Users\tiesh\Documents\Create-Train-Parts\src\main\resources\assets\create_train_parts\models\block\train_step_andesite'

# Output folder for the split models


# Directions to process
directions = ["north", "east", "south", "west", "up", "down"]

input = input("Generate (g) or Remove (r) models? ")

# Process each JSON file in the folder
if os.path.exists(folder_path):
    for filename in os.listdir(folder_path):
        if filename.endswith('.json') and len(filename.split("_")) > 1:
            if input == "g":
                with open(os.path.join(folder_path, filename), 'r') as file:
                    model_data = json.load(file)

                # print(f"Processing {filename}")
                
                # Process each direction
                for direction in directions:
                    # Create a copy of the model data
                    new_model = {
                        "credit": model_data.get("credit", ""),
                        "textures": model_data.get("textures", {}),
                        "elements": [],
                        "groups": model_data.get("groups", [])
                    }
                
                    # Filter elements to include only the specified direction
                    for element in model_data.get("elements", []):
                        new_faces = {}
                        if "faces" in element and direction in element["faces"]:
                            # Handle exceptions for slide_east/slide_west
                            if filename.startswith("slide_east") or filename.startswith("slide_west"):
                                if direction == "north":
                                    # Include both north and up sides in the same file
                                    if "north" in element["faces"]:
                                        new_faces["north"] = element["faces"]["north"]
                                    if "up" in element["faces"]:
                                        new_faces["up"] = element["faces"]["up"]
                                elif direction == "up":
                                    # Make the up side empty
                                    continue
                
                            # Handle exceptions for pivot_east/pivot_west
                            elif filename.startswith("pivot_east") or filename.startswith("pivot_west"):
                                if direction == "up":
                                    # Include both up and south sides in the same file
                                    if "up" in element["faces"]:
                                        new_faces["up"] = element["faces"]["up"]
                                    if "south" in element["faces"]:
                                        new_faces["south"] = element["faces"]["south"]
                                elif direction == "south":
                                    # Make the south side empty
                                    continue
                                
                            elif filename.startswith("steps_east") or filename.startswith("steps_west"):
                                print(f"Processing {filename}")
                                # Check if the element belongs to the Bottom2 group
                                bottom2_elements = []
                                if "groups" in model_data:
                                    for group in model_data["groups"]:
                                        if isinstance(group, dict) and group.get("name") == "stairs":
                                            for child in group.get("children", []):
                                                if isinstance(child, dict) and child.get("name") == "Bottom":
                                                    for child2 in child.get("children", []):
                                                        if isinstance(child2, dict) and child2.get("name") == "Bottom2":
                                                            # Collect elements in the Bottom2 group
                                                            for child_index in child2.get("children", []):
                                                                if isinstance(child_index, int) and child_index < len(model_data["elements"]):
                                                                    bottom2_elements.append(model_data["elements"][child_index])
                            
                                # Process all elements, including Bottom2
                                for element in model_data.get("elements", []):
                                    new_faces = {}
                                    if element in bottom2_elements:
                                        # Special handling for Bottom2 elements
                                        if "faces" in element:
                                            if direction == "north":
                                                # Include both north and up sides in the same file
                                                if "north" in element["faces"]:
                                                    new_faces["north"] = element["faces"]["north"]
                                                if "up" in element["faces"]:
                                                    new_faces["up"] = element["faces"]["up"]
                                            elif direction == "up":
                                                # Make the up side empty
                                                continue
                                    else:
                                        # Default behavior for other elements
                                        if "faces" in element and direction in element["faces"]:
                                            new_faces[direction] = element["faces"][direction]
                            
                                    if new_faces:
                                        new_element = element.copy()
                                        new_element["faces"] = new_faces
                                        new_model["elements"].append(new_element)
                                                                
                                         
                            # Default behavior for other cases
                            else:
                                new_faces[direction] = element["faces"][direction]
                
                        if new_faces:
                            new_element = element.copy()
                            new_element["faces"] = new_faces
                            new_model["elements"].append(new_element)
                
                    # If no elements are left, create an empty model
                    if not new_model["elements"]:
                        new_model["elements"] = []
                
                    # Save the new model to a file
                    output_file = os.path.join(folder_path, f"{os.path.splitext(filename)[0]}_{direction}.json")
                    with open(output_file, 'w') as out_file:
                        json.dump(new_model, out_file, indent=4)
                    # print(f"Saved {output_file}")
            elif input == "r":
                # Remove the split models
                for direction in directions:
                    output_file = os.path.join(folder_path, f"{os.path.splitext(filename)[0]}_{direction}.json")
                    if os.path.exists(output_file):
                        os.remove(output_file)
                        # print(f"Removed {output_file}")

