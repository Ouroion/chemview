# ChemView

ChemView is a tool built with APIVerve's Periodic Table API that allows users to request any element from the periodic table and get additional information about it, including atomic number, atomic mass, density, etc.

## Project Structure

ChemView is an application consisting of:
-**Backend**:
  - Element information with APIVerve Periodic Table API

## Prerequisites

Before setting up Chemview, ensure you have the following installed:

### Required Software

#### Backend Requirements
1. **Java SDK 11**: ChemView uses the temurin-11 SDK. 
2. **Maven**: included as part of the project
3. **APIVerve API Key**: Required for Periodic table API integration
    - Obtain an API key by signing up for an APIVerve account from [APIVerve] https://apiverve.com/

## API Configuration
Chemview requires the Periodic Table API key from APIVerve to use its services. This should be configured directly in the application configuration files.

### Required API Keys
| Key                    | Description                                                        | How to Obtain | Required APIs                                         |
|------------------------|--------------------------------------------------------------------|---------------|-------------------------------------------------------|
| Periodic Table API Key | APIVerve Periodic table API Key for obtaining element information. | [APIVerve] https://apiverve.com/ | Periodic Table API  

### Setting API Key

Configure your API keys in the .env configuration file

**For Production/Development**:
Edit `.env`:
```env
    PERIODIC_TABLE_API_KEY=YOUR_PERIODIC_TABLE_API_KEY
```